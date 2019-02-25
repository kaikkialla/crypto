package banana.digital.crypto.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import banana.digital.crypto.R;
import banana.digital.crypto.repository.WalletRepository;

public class InfoFragment extends Fragment {
    Button button;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.info_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        button = view.findViewById(R.id.btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WalletRepository.getInstance().sendTransaction();
            }
        });
    }




    private static class Presenter {

        public static Presenter instance;
        public static Presenter getInstance() {
            if (instance == null) { instance = new Presenter(); }
            return instance;
        }



    }
}



