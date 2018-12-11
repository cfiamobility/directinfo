package ca.gc.inspection.directinfo;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class DirectInfoAdapter extends RecyclerView.Adapter<DirectInfoAdapter.ViewHolder> {

    private Context context;

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView personNameTV;
//        TextView personEmailTV;
//        TextView personPhoneTV;
        TextView personPositionTV;
//        ImageView imageView;
//        TextView personAddressTV;


        ViewHolder(View itemView) {
            super(itemView);

            personNameTV = itemView.findViewById(R.id.personNameTV);
//            personEmailTV = itemView.findViewById(R.id.personEmailTV);
//            personPhoneTV = itemView.findViewById(R.id.personPhoneTV);
            personPositionTV = itemView.findViewById(R.id.personPositionTV);
//            personAddressTV = itemView.findViewById(R.id.personAddressTV);
//            imageView = itemView.findViewById(R.id.imageView);

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
//        View peopleView = inflater.inflate(R.layout.search_result_row, viewGroup, false);

//        return new ViewHolder(peopleView);
        return new ViewHolder(inflater.inflate(R.layout.search_result_row, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.personNameTV.setText(people.get(i).getName());
//        viewHolder.personEmailTV.setText(people.get(i).getEmail());
//        viewHolder.personPhoneTV.setText(people.get(i).getPhone());
        viewHolder.personPositionTV.setText(people.get(i).getTitle());
//        viewHolder.personAddressTV.setText(people.get(i).getAddress());

    }

    @Override
    public int getItemCount() {
        return people.size();
    }

    public Person getPerson(int position) {
        return ((people != null) && (people.size() != 0) ? people.get(position) : null);
    }
}