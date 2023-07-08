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
import com.example.blackjack.ui.view_models.GamerViewModel;

public class StartFragment extends Fragment {
    private FragmentStartBinding binding;

    private NavController navController;

    private GamerViewModel gamerViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentStartBinding.inflate(inflater, container, false);

        NavHostFragment navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
        assert navHostFragment != null;
        navController = navHostFragment.getNavController();

        gamerViewModel = new ViewModelProvider(requireActivity()).get(GamerViewModel.class);

        return binding.getRoot();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        gamerViewModel.getGamer().observe(getViewLifecycleOwner(), gamerEntity -> {
            if (gamerEntity == null) {
                System.out.println(true);
                gamerViewModel.gamer = new GamerEntity(1000);
                gamerViewModel.insertGamer(gamerViewModel.gamer);
            }
            else {
                System.out.println(false);
                gamerViewModel.gamer = gamerEntity;
            }


            binding.balanceTextView.setText("Баланс: " + gamerViewModel.gamer.balance);

            binding.enterRoomButton.setOnClickListener(view1 -> navController.navigate(R.id.action_startFragment_to_gameRoomFragment));
        });


    }
}