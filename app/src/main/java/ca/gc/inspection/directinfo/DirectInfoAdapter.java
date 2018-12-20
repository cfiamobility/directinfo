package ca.gc.inspection.directinfo;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class DirectInfoAdapter extends RecyclerView.Adapter<DirectInfoAdapter.ViewHolder> {

    private Context context;

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView personNameTV;
        TextView personPositionTV;
        TextView tvDepartmentOrAddress;


        ViewHolder(View itemView) {
            super(itemView);

            personNameTV = itemView.findViewById(R.id.personNameTV);
            personPositionTV = itemView.findViewById(R.id.personPositionTV);
        }
    } // end of ViewHolder class

    private ArrayList<Person> people;

    DirectInfoAdapter(ArrayList<Person> people, Context context){
        this.people = people;
        this.context = context;
    }

    @NonNull
    @Override
    public DirectInfoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);

        return new ViewHolder(inflater.inflate(R.layout.search_result_row, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        viewHolder.personNameTV.setText(people.get(i).getName());
        viewHolder.personPositionTV.setText(people.get(i).getTitle());

//        if (searchByAddress) {
//            viewHolder.tvDepartmentOrAddress.setText(people.get(i).getPhysicalAddress());
//        } else {
//            viewHolder.tvDepartmentOrAddress.setText(R.string.cfia);
//        }
    }

    @Override
    public int getItemCount() {
        return people.size();
    }

    public Person getPerson(int position) {
        return ((people != null) && (people.size() != 0) ? people.get(position) : null);
    }
}