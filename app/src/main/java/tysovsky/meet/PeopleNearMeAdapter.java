package tysovsky.meet;

import android.content.Context;
import android.provider.Contacts;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tysovsky on 4/4/16.
 */
public class PeopleNearMeAdapter extends ArrayAdapter<PeopleNearMe> {

    public PeopleNearMeAdapter(Context context, List<PeopleNearMe> peopleNearMe){
        super(context, 0, peopleNearMe);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.view_people_near_me, parent, false);
        }

        PeopleNearMe currentPerson = getItem(position);

        ImageView avatar = (ImageView)convertView.findViewById(R.id.pnm_avatar);
        avatar.setTag(new Integer(position));
        avatar.setOnClickListener(avatarOnClickListener);

        TextView nameView = (TextView)convertView.findViewById(R.id.pnm_name);
        TextView usernameView = (TextView)convertView.findViewById(R.id.pnm_username);

        nameView.setText(currentPerson.getName());
        usernameView.setText(currentPerson.getUsername());

        return convertView;
    }

    private View.OnClickListener avatarOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

        }
    };
}
