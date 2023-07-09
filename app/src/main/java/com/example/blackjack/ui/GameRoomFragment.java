package com.example.blackjack.ui;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.blackjack.R;
import com.example.blackjack.data.DialogCallback;
import com.example.blackjack.data.data_sources.models.Player;
import com.example.blackjack.data.data_sources.room.entites.CardEntity;
import com.example.blackjack.databinding.FragmentGameRoomBinding;
import com.example.blackjack.ui.view_models.CardViewModel;
import com.example.blackjack.ui.view_models.GamerViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class GameRoomFragment extends Fragment {

    private FragmentGameRoomBinding binding;

    private GamerViewModel gamerViewModel;
    private CardViewModel cardViewModel;

    private int bet;
    private List<CardEntity> deck;

    private Player player;
    private Player dealer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentGameRoomBinding.inflate(inflater, container, false);

        gamerViewModel = new ViewModelProvider(requireActivity()).get(GamerViewModel.class);
        cardViewModel = new ViewModelProvider(requireActivity()).get(CardViewModel.class);


        return binding.getRoot();
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        showPurchaseDialog("Купить сигару?", "cigar", "Да (150$)", 150, () -> showPurchaseDialog("Купить виски?", "whiskey", "Да (100$)", 100, this::startGame));


        binding.addButton.setOnClickListener(view1 -> {
            addCard(false);
            checkCardImagesVisible(player);
            setSums(false);
            if (player.getSum(true) > 21) {
                try {
                    finishGame();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        binding.doubleButton.setOnClickListener(view12 -> {
            addCard(false);
            bet*=2;
            try {
                finishGame();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        binding.stopButton.setOnClickListener(view13 -> {
            try {
                finishGame();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

    }

    @SuppressLint("DiscouragedApi")
    private void showPurchaseDialog(String questionText, String imageName, String confirmButtonText, int cost, DialogCallback callback) {
        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.shop_dialog);

        ((TextView) dialog.findViewById(R.id.question_textview)).setText(questionText);
        ((TextView) dialog.findViewById(R.id.description_textview)).setText("Это скрасит вашу игру, создав уютную обстановку!");

        Button confirmButton = dialog.findViewById(R.id.confirm_button);
        Button cancelButton = dialog.findViewById(R.id.cancel_button);
        confirmButton.setText(confirmButtonText);
        confirmButton.setOnClickListener(view -> {
            if (gamerViewModel.gamer.balance < cost) {
                Toast.makeText(requireContext(), "Недостаточно средств!", Toast.LENGTH_SHORT).show();
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    if (Objects.equals(imageName, "cigar"))
                        binding.sigarImageView.setImageResource(getResources().getIdentifier(imageName, "drawable", view.getContext().getOpPackageName()));
                    else
                        binding.whiskeyImageView.setImageResource(getResources().getIdentifier(imageName, "drawable", view.getContext().getOpPackageName()));
                }
                gamerViewModel.gamer.balance -= cost;
                dialog.dismiss();
                try {
                    callback.onDialogDismiss();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });
        cancelButton.setOnClickListener(view -> {
            dialog.dismiss();
            try {
                callback.onDialogDismiss();
                System.out.println(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        dialog.show();
    }

    @SuppressLint("SetTextI18n")
    private void showBetDialog(DialogCallback callback) {
        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.bet_dialog);

        TextView balanceTextView = dialog.findViewById(R.id.balance_textview);
        balanceTextView.setText(balanceTextView.getText() + " " + player.getBalance());

        dialog.findViewById(R.id.bet_button).setOnClickListener(view1 -> {
            try {
                if (Integer.parseInt(((EditText) dialog.findViewById(R.id.bet_edittext)).getText().toString()) <= gamerViewModel.gamer.balance) {
                    bet = Integer.parseInt(((EditText) dialog.findViewById(R.id.bet_edittext)).getText().toString());
                    dialog.dismiss();
                    callback.onDialogDismiss();
                } else {
                    Toast.makeText(requireContext(), "Недостаточно средст! Ваш баланс: " + gamerViewModel.gamer.balance, Toast.LENGTH_LONG).show();
                }

            } catch (NumberFormatException e) {
                e.printStackTrace();
                Toast.makeText(requireContext(), "Неверный формат ввода!", Toast.LENGTH_SHORT).show();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        dialog.show();
    }

    private void checkCardImagesVisible(Player unit) {
        ImageView[] dealerImageViews = {
                binding.firstDealerCardImageView,
                binding.secondDealerCardImageView,
                binding.thirdDealerCardImageView,
                binding.fourthDealerCardImageView,
                binding.fifthDealerCardImageView
        };

        ImageView[] playerImageViews = {
                binding.firstPlayerCardImageView,
                binding.secondPlayerCardImageView,
                binding.thirdPlayerCardImageView,
                binding.fourthPlayerCardImageView,
                binding.fifthPlayerCardImageView
        };

        ImageView[] imageViews = unit.isDealer() ? dealerImageViews : playerImageViews;
        int numCards = unit.getCards().size();

        for (int i = 0; i < imageViews.length; i++) {
            if (i < numCards) {
                imageViews[i].setVisibility(View.VISIBLE);
            } else {
                imageViews[i].setVisibility(View.GONE);
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private void setSums(boolean isLast){
        binding.dealerTextview.setText("Диллер (сумма: " + dealer.getSum(isLast) + "):");
        binding.playerTextview.setText("Игрок\n(сумма: " + player.getSum(true) + "):");

    }

    private void addCard(boolean isDealer) {
        Random random = new Random();
        int cardIndex = random.nextInt(deck.size());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            Player unit = isDealer ? dealer : player;
            ImageView[] imageViews = isDealer ? new ImageView[] {
                    binding.firstDealerCardImageView,
                    binding.secondDealerCardImageView,
                    binding.thirdDealerCardImageView,
                    binding.fourthDealerCardImageView,
                    binding.fifthDealerCardImageView
            } : new ImageView[] {
                    binding.firstPlayerCardImageView,
                    binding.secondPlayerCardImageView,
                    binding.thirdPlayerCardImageView,
                    binding.fourthPlayerCardImageView,
                    binding.fifthPlayerCardImageView
            };

            unit.addCard(deck.get(cardIndex));
            deck.remove(cardIndex);
            for (int i = 0; i < unit.getCards().size(); i++) {
                CardEntity card = unit.getCards().get(i);
                if (i == 0 && unit.isDealer()){
                    @SuppressLint("DiscouragedApi") int resourceId = getResources().getIdentifier("card", "drawable", requireContext().getOpPackageName());
                    imageViews[i].setImageResource(resourceId);
                    continue;
                }
                @SuppressLint("DiscouragedApi") int resourceId = getResources().getIdentifier(card.imgSrc, "drawable", requireContext().getOpPackageName());
                imageViews[i].setImageResource(resourceId);
            }
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void startGame() throws InterruptedException {
        player = new Player(false, gamerViewModel.gamer.balance, new ArrayList<>());
        dealer = new Player(true, 0, new ArrayList<>());
        showBetDialog(() -> {
            deck = cardViewModel.cards;
            checkCardImagesVisible(player);
            checkCardImagesVisible(dealer);

            for (int i = 0; i < 4; i++){
                addCard(i % 2 == 0);
            }
            checkCardImagesVisible(player);
            checkCardImagesVisible(dealer);
            setSums(false);
            if (player.getSum(true) == 21 || dealer.getSum(true) == 21) {
                bet = (int) Math.round(bet * 1.25);
                finishGame();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void finishGame() throws InterruptedException {
        @SuppressLint("DiscouragedApi") int resourceId = getResources().getIdentifier(dealer.getCards().get(0).imgSrc, "drawable", requireContext().getOpPackageName());
        binding.firstDealerCardImageView.setImageResource(resourceId);
        if (player.getCards().size() == 2 && player.getSum(true) == 21){
            player.setBalance(player.getBalance() + bet);
            restartGame("У вас блэк джек!. Вы выиграли");
            return;
        }
        if (dealer.getCards().size() == 2 && dealer.getSum(true) == 21){
            player.setBalance(player.getBalance() - bet);
            restartGame("У крупье блэк джек!. Вы проиграли");
            return;
        }
        if (player.getSum(true) > 21){
            player.setBalance(player.getBalance() - bet);
            restartGame("У вас перебор. Вы проиграли");
            return;
        }
        else {
            while (dealer.getSum(true) < 17) addCard(true);
        }
        setSums(true);
        checkCardImagesVisible(player);
        checkCardImagesVisible(dealer);
        binding.firstDealerCardImageView.setImageResource(resourceId);

        if (dealer.getSum(true) > 21){
            player.setBalance(player.getBalance() + bet);
            restartGame("У крупье перебор. Вы выиграли");
            return;
        }

        if (dealer.getSum(true) < player.getSum(true)) {
            player.setBalance(player.getBalance() + bet);
            restartGame("У вас сумма больше. Вы выиграли");
            return;
        }

        if (dealer.getSum(true) == player.getSum(true)) {
            restartGame("У вас равные суммы. Ничья");
            return;
        }

        if (dealer.getSum(true) > player.getSum(true)) {
            player.setBalance(player.getBalance() - bet);
            restartGame("У вас сумма меньше. Вы проиграли");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void restartGame(String toastMessage){
        gamerViewModel.gamer.balance = player.getBalance();
        deck = cardViewModel.cards;
        Toast.makeText(requireContext(), toastMessage, Toast.LENGTH_LONG).show();
        try {
            startGame();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}