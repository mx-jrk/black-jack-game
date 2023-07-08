package com.example.blackjack.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.blackjack.R;
import com.example.blackjack.data.data_sources.room.entites.GamerEntity;
import com.example.blackjack.databinding.FragmentStartBinding;
import com.example.blackjack.ui.view_models.CardViewModel;
import com.example.blackjack.ui.view_models.GamerViewModel;

public class StartFragment extends Fragment {
    private FragmentStartBinding binding;

    private NavController navController;

    private GamerViewModel gamerViewModel;
    private CardViewModel cardViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentStartBinding.inflate(inflater, container, false);

        NavHostFragment navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
        assert navHostFragment != null;
        navController = navHostFragment.getNavController();

        gamerViewModel = new ViewModelProvider(requireActivity()).get(GamerViewModel.class);
        cardViewModel = new ViewModelProvider(requireActivity()).get(CardViewModel.class);

        return binding.getRoot();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //Uploading user information
        gamerViewModel.getGamer().observe(getViewLifecycleOwner(), gamerEntity -> {
            if (gamerEntity == null) {
                gamerViewModel.gamer = new GamerEntity(1000);
                gamerViewModel.isFirstLaunch = true;
            }
            else {
                gamerViewModel.gamer = gamerEntity;
                gamerViewModel.isFirstLaunch = false;
            }
            //Loading cards
            cardViewModel.getAllCards().observe(getViewLifecycleOwner(), cardEntities -> {
                cardViewModel.cards = cardEntities;

                //Setting values after loading data
                binding.balanceTextView.setText("Баланс: " + gamerViewModel.gamer.balance);

                binding.enterRoomButton.setOnClickListener(view1 -> navController.navigate(R.id.action_startFragment_to_gameRoomFragment));
            });

        });


    }
}