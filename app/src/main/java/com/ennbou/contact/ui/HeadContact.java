package com.ennbou.contact.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;

import android.graphics.Rect;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ItemDecoration;

import com.ennbou.contact.R;
import com.ennbou.contact.data.Contact;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.ennbou.contact.R.color.secondaryTextColor;

public class HeadContact extends ItemDecoration {

    private List<Contact> contacts;
    private HashMap<Integer, String> headers;
    private Context context;

    public HeadContact(List<Contact> contacts, Context context) {
        this.contacts = contacts;
        this.context = context;
        headers = getHeaderLetter(contacts);
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        outRect.top = (headers.get(position) != null) ? 24 : 0;
    }

    @Override
    public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {

        if (headers == null || parent.getChildCount() <= 0) return;

        float parentPadding = parent.getPaddingTop();

        int earliestPosition = Integer.MAX_VALUE;
        int previousHeaderPosition = -1;
        boolean previousHasHeader = false;
        View earliestChild = null;

        for (int i = parent.getChildCount() - 1; i >= 0; i--) {


            View child = parent.getChildAt(i);
            if (child == null) continue;

            if (child.getY() > parent.getHeight() || (child.getY() + child.getHeight()) < 0) {
                continue;
            }

            int position = parent.getChildAdapterPosition(child);

            if (position < 0) {
                continue;
            }

            if (position < earliestPosition) {
                earliestPosition = position;
                earliestChild = child;
            }

            String header = headers.get(position);


            if (header != null) {
                drawHeader(c, child, parentPadding, header, previousHasHeader);
                previousHeaderPosition = position;
                previousHasHeader = true;
            } else {
                previousHasHeader = false;
            }
        }

        if (earliestChild != null && earliestPosition != previousHeaderPosition) {
            String text = String.valueOf(contacts.get(earliestPosition).getFirstName().charAt(0));
            text = text.toUpperCase();
            previousHasHeader = previousHeaderPosition - earliestPosition == 1;
            drawHeader(c, earliestChild, parentPadding, text, previousHasHeader);
        }
    }

    // 0 Ahmmed jjj
    // 1 Ahmmed iii
    // 2 Ahmmed iii
    // 3 Ahmmed iii
    // 4 Ahmmed iii
    // 5 Bouchaib jjj
    // 6 Bouchaib iii

    // 0
    // 5
    // 9
    // 17


    private HashMap<Integer, String> getHeaderLetter(List<Contact> contacts) {
        if (contacts == null) return new HashMap<>();
        List<Character> list = new ArrayList<>();
        HashMap<Integer, String> map = new HashMap<>();
        for (int i = 0; i < contacts.size(); i++) {
            char letter = contacts.get(i).getFirstName().charAt(0);
            letter = Character.toUpperCase(letter);
            if (list.contains(letter)) continue;
            list.add(letter);
            String text = String.valueOf(letter);
            map.put(i, text);
        }
        return map;
    }


    private void drawHeader(Canvas canvas, View child, float parentPadding, String header, Boolean previousHasHeader) {

        TextPaint mTextPaint = new TextPaint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(60);

        TypedArray attrs = context.obtainStyledAttributes(R.style.ContactHeaders, R.styleable.HeaderContact);
        int color = attrs.getColor(R.styleable.HeaderContact_android_textColor, 0);
//        Typeface typeFace = ResourcesCompat.getFont(context,
//                attrs.getResourceId(R.styleable.HeaderContact_android_fontFamily, 0)
//        );
//        mTextPaint.setTypeface(typeFace);
        mTextPaint.setColor(color);

        float padding = 16;

        float childTop = child.getY();
        float childBottom = childTop + child.getHeight();

        Rect bounds = new Rect();
        mTextPaint.getTextBounds(header, 0, header.length(), bounds);

        float top = Math.max(childTop, parentPadding) + bounds.height();

        if (previousHasHeader) {
            top = Math.min(top, childBottom - bounds.height());
        }

        int headerAlpha = 100 * 255;
        mTextPaint.setAlpha(headerAlpha);

        int count = canvas.save();
        canvas.translate(24, top);
        canvas.drawText(header, 0, padding, mTextPaint);
        canvas.restoreToCount(count);
    }

}
