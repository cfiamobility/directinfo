package ca.gc.inspection.directinfo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper.Callback;
import android.view.MotionEvent;
import android.view.View;

import static android.support.v7.widget.helper.ItemTouchHelper.*;

enum ButtonState {
    GONE,
    LEFT_VISIBLE,
    RIGHT_VISIBLE
}

public class SwipeGestureController extends Callback {

    private boolean swipeBack = false;
    private ButtonState buttonState = ButtonState.GONE;
    private static final float buttonWidth = 500;

    RectF rectInstance;

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(0, LEFT | RIGHT);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

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
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                            float dX, float dY, int actionState, boolean isCurrentlyActive) {

        if (actionState == ACTION_STATE_SWIPE) {
            setTouchListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

        drawButtons(c, viewHolder);
    }

    private void drawButtons(Canvas c, RecyclerView.ViewHolder viewHolder) {
        float buttonWidthWithoutPadding = buttonWidth - 20;
        float corners = 16;

        View itemView = viewHolder.itemView;
        Paint paint = new Paint();
        Drawable icon = ContextCompat.getDrawable(viewHolder.itemView.getContext(), R.drawable.ic_call_black_24dp);

//        RectF callSwipeLeftRect = new RectF(itemView.getLeft(), itemView.getTop() + 100,itemView.getLeft() + buttonWidthWithoutPadding, itemView.getBottom() - 80);
        RectF callSwipeLeftRect = new RectF(itemView.getLeft(), itemView.getTop() + 100,itemView.getLeft() + buttonWidthWithoutPadding, itemView.getBottom() - 80);
        paint.setColor(Color.parseColor("#388E3C"));
        c.drawRoundRect(callSwipeLeftRect, corners, corners, paint);
        drawText( c, callSwipeLeftRect, paint, icon);

//        RectF emailSwipeRightRect = new RectF(itemView.getRight() - buttonWidthWithoutPadding, itemView.getTop() + 100, itemView.getRight(), itemView.getBottom() - 80);
        RectF emailSwipeRightRect = new RectF(itemView.getRight() - buttonWidthWithoutPadding, itemView.getTop() + 100, itemView.getRight(), itemView.getBottom() - 80);
        paint.setColor(Color.parseColor("#FF9800"));
        c.drawRoundRect(emailSwipeRightRect, corners, corners, paint);
        icon = ContextCompat.getDrawable(viewHolder.itemView.getContext(), R.drawable.ic_email_black_24dp);
        drawText( c, emailSwipeRightRect, paint, icon);



        rectInstance = null;
        if (buttonState == ButtonState.LEFT_VISIBLE) rectInstance = callSwipeLeftRect;
        else if (buttonState == ButtonState.RIGHT_VISIBLE) rectInstance = emailSwipeRightRect;

    }

    private void drawText(Canvas c, RectF rectangle, Paint paint, Drawable icon) {

/*
        float textSize = 60;
        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);
        paint.setTextSize(textSize);

        float textWidth = paint.measureText(text);
        c.drawText(text, rectangle.centerX() - (textWidth/2), rectangle.centerY() + (textSize/2), paint);
*/

        icon.setBounds(((int) rectangle.left), ((int) rectangle.top), ((int) rectangle.right), ((int) rectangle.bottom));
        icon.draw(c);

    }

    private void setTouchListener(final Canvas c, final RecyclerView recyclerView, final RecyclerView.ViewHolder viewHolder,
                                  final float dX, final float dY, final int actionState, final boolean isCurrentlyActive){

        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                swipeBack = event.getAction() == MotionEvent.ACTION_CANCEL ||
                        event.getAction() == MotionEvent.ACTION_UP;

                if (swipeBack) {
                    if (dX < -buttonWidth) buttonState = ButtonState.RIGHT_VISIBLE;
                    else if (dX > buttonWidth) buttonState = ButtonState.LEFT_VISIBLE;

                    if (buttonState != ButtonState.GONE) {
                        setTouchDownListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                        setItemsClickable(recyclerView, false);
                    }
                }

                return false;
            }
        });

    }

    private void setTouchDownListener(final Canvas c, final RecyclerView recyclerView, final RecyclerView.ViewHolder viewHolder,
                                      final float dX, final float dY, final int actionState, final boolean isCurrentlyActive){

        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    setTouchUpListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                }

                return false;
            }
        });
    }

    private void setTouchUpListener(final Canvas c, final RecyclerView recyclerView, final RecyclerView.ViewHolder viewHolder,
                                    final float dX, final float dY, final int actionState, final boolean isCurrentlyActive){

        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    SwipeGestureController.super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                    recyclerView.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            return false;
                        }
                    });

                    setItemsClickable(recyclerView, true);
                    swipeBack = false;
                    buttonState = ButtonState.GONE;
                }

                return false;
            }
        });
    }

    private void setItemsClickable(RecyclerView recyclerView, boolean isClickable) {
        for (int i = 0; i < recyclerView.getChildCount(); i++) {
            recyclerView.getChildAt(i).setClickable(isClickable);
        }
    }

} // end of class
