package tysovsky.meet;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


/**
 *This is the fragment that displays people near you
 */
public class FragmentPeopleNearMe extends BaseFragment {

    public static String TAG = "FragmentPeopleNearMe";
    ListView peopleNearMeList = null;
    ArrayList<PeopleNearMe> peopleNearMe = null;
    PeopleNearMeAdapter pnmAdapter = null;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     */
    public static FragmentPeopleNearMe newInstance(String param1, String param2) {
        FragmentPeopleNearMe fragment = new FragmentPeopleNearMe();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentPeopleNearMe() {
        // Required empty public constructor
        peopleNearMe = new ArrayList<PeopleNearMe>();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);

        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_people_near_me, container, false);

        peopleNearMeList = (ListView)view.findViewById(R.id.people_near_me_list);
        pnmAdapter = new PeopleNearMeAdapter(getActivity(), peopleNearMe);
        peopleNearMeList.setAdapter(pnmAdapter);
        peopleNearMeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((MainActivity)getActivity()).profileViewRequested(peopleNearMe.get(position));
                //Toast.makeText(getActivity(), peopleNearMe.get(position).getName() + " clicked!", Toast.LENGTH_LONG).show();
            }
        });

        showFab();
        return view;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    public void updatePeopleNearMe(ArrayList<PeopleNearMe> peopleNearMe){

        this.peopleNearMe.clear();
        this.peopleNearMe.addAll(peopleNearMe);
        pnmAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


}
