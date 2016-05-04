package tysovsky.meet;

import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;

/**
 * Created by tysovsky on 4/4/16.
 */
public abstract class BaseFragment extends Fragment {
    public boolean CURRENTLY_VISIBLE = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        CURRENTLY_VISIBLE = true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        CURRENTLY_VISIBLE = false;
    }

    public boolean isCurrentlyVisible(){
        return CURRENTLY_VISIBLE;
    }

    public void showFab(){
        if(getActivity() instanceof MainActivity){
            ((MainActivity)getActivity()).showFab();
        }
    }

    public void hideFab(){
        if(getActivity() instanceof MainActivity){
            ((MainActivity)getActivity()).hideFab();
        }
    }

    public FloatingActionButton getFab(){
        if(getActivity() instanceof MainActivity){
            return ((MainActivity)getActivity()).getFab();
        }
        else{
            return null;
        }
    }
}
