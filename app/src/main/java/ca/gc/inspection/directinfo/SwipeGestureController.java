package ca.gc.inspection.directinfo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper.Callback;
import android.view.MotionEvent;
import android.view.View;

import static android.support.v4.content.ContextCompat.startActivity;
import static android.support.v7.widget.helper.ItemTouchHelper.*;

public class SwipeGestureController extends Callback {

    private boolean swipeBack = false;

    private DirectInfoAdapter adapter;

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        adapter = (DirectInfoAdapter) recyclerView.getAdapter();
        return makeMovementFlags(0, LEFT | RIGHT);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
    }

    @Override
    public int convertToAbsoluteDirection(int flags, int layoutDirection) {

        if (swipeBack) {
            swipeBack = false;
            return 0;
        }

        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder,
                            float dX, float dY, int actionState, boolean isCurrentlyActive) {

        if (actionState == ACTION_STATE_SWIPE) {
            float width;
            width = (float) viewHolder.itemView.getWidth();
            float alpha = 1.0f - Math.abs(dX) / width;
            viewHolder.itemView.setAlpha(alpha);
            viewHolder.itemView.setTranslationX(dX);

            drawButtons(c, viewHolder, actionState, dX);
            setTouchListener(recyclerView, viewHolder, dX);
        } else {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY,
                    actionState, isCurrentlyActive);
        }
    }

    private void drawButtons(Canvas c, RecyclerView.ViewHolder viewHolder, int actionState, float dX) {

        View itemView = viewHolder.itemView;
        Rect rectangle= new Rect(itemView.getLeft(), itemView.getTop() ,itemView.getRight(), itemView.getBottom() );

        if (actionState == ACTION_STATE_SWIPE && dX > 0) {
            Drawable icon = ContextCompat.getDrawable(itemView.getContext(), R.drawable.swipe_call_image);
            assert icon != null;
            icon.setBounds(rectangle.left, rectangle.top, rectangle.right, rectangle.bottom);
            icon.draw(c);
        }
        else if (actionState == ACTION_STATE_SWIPE && dX < 0) {
            Drawable icon = ContextCompat.getDrawable(itemView.getContext(), R.drawable.swipe_email_image);
            assert icon != null;
            icon.setBounds(rectangle.left, rectangle.top, rectangle.right, rectangle.bottom);
            icon.draw(c);
        }
    }

@SuppressLint("ClickableViewAccessibility")
private void setTouchListener(final RecyclerView recyclerView, final RecyclerView.ViewHolder viewHolder,
                              final float dX){

    recyclerView.setOnTouchListener(new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            swipeBack = event.getAction() == MotionEvent.ACTION_CANCEL ||
                    event.getAction() == MotionEvent.ACTION_UP;

            View childView = recyclerView.findChildViewUnder(event.getX(), event.getY());

            if (childView != null) {

                if (swipeBack) {
                    if (dX < -(viewHolder.itemView.getRight() - 150) ) {
                        Intent intent = new Intent(Intent.ACTION_SENDTO);
                        intent.setData(Uri.parse("mailto:"));
                        intent.putExtra(Intent.EXTRA_EMAIL, new String[] {adapter.getPerson(viewHolder.getAdapterPosition()).getEmail()});
//                                Log.d("EMAIL IN GESTURE DETECTOR", "onTouch: " + adapter.getPerson(viewHolder.getAdapterPosition()).getEmail());
                        startActivity(recyclerView.getContext(), intent, null);
                    } else if (dX > (viewHolder.itemView.getRight() - 150)) {
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:" + adapter.getPerson(viewHolder.getAdapterPosition()).getPhone()));
                        startActivity(recyclerView.getContext(), intent, null);
                    }
                }
            }
            return false;
        }
    });
}

} // end of class
