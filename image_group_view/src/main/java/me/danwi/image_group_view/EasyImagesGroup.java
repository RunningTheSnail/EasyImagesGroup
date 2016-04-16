package me.danwi.image_group_view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by RunningSnail on 16/4/15.
 */
public class EasyImagesGroup extends ViewGroup implements View.OnClickListener {

    private Context context;

    //行个数
    private int lineCount;

    //纵向个数
    private int vCount;

    //横向间隔
    private int vSpace;

    //纵向间隔
    private int hSpace;

    private DealBusiness dealBusiness;

    public EasyImagesGroup(Context context) {
        super(context);
    }

    public EasyImagesGroup(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EasyImagesGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.EasyImagesGroup, defStyleAttr, 0);
        lineCount = typedArray.getInteger(R.styleable.EasyImagesGroup_lineCount, 3);
        vSpace = (int) typedArray.getDimension(R.styleable.EasyImagesGroup_vSpace, 10);
        hSpace = (int) typedArray.getDimension(R.styleable.EasyImagesGroup_hSpace, 10);
        typedArray.recycle();
    }

    public void setDealBusiness(DealBusiness dealBusiness) {
        this.dealBusiness = dealBusiness;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int count = getChildCount();

        //先测量一波
        for (int i = 0; i < count; i++) {
            View view = getChildAt(i);
            measureChild(view, widthMeasureSpec, heightMeasureSpec);
        }

        if (widthMode == MeasureSpec.AT_MOST) {
            if (count > 0) {
                //每一个子元素width,height都是一样的
                if (count >= lineCount) {
                    widthSize = (lineCount - 1) * hSpace + lineCount * getChildAt(0).getMeasuredWidth();
                } else {
                    widthSize = (count - 1) * hSpace + count * getChildAt(0).getMeasuredWidth();
                }
            } else {
                widthSize = 0;
            }
        }

        if (heightMode == MeasureSpec.AT_MOST) {
            if (count % lineCount != 0) {
                vCount = count / lineCount + 1;
            } else {
                vCount = count / lineCount;
            }
            if (count > 0) {
                heightSize = (vCount - 1) * vSpace + vCount * getChildAt(0).getMeasuredHeight();
            }
        }


        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int count = getChildCount();

        for (int i = 0; i < count; i++) {
            View view = getChildAt(i);

            view.setTag(i);

            //判断位置
            int temp = i + 1;

            //水平方向位置
            int h = temp % lineCount;
            //竖直方向位置
            int v;
            if (h == 0) {
                h = lineCount;
                v = temp / lineCount;
            } else {
                v = temp / lineCount + 1;
            }

            int left = (h - 1) * getChildAt(0).getMeasuredWidth() + (h - 1) * hSpace;

            int top = (v - 1) * getChildAt(0).getMeasuredHeight() + (v - 1) * vSpace;

            view.layout(left, top, left + view.getMeasuredWidth(), top + view.getMeasuredHeight());

            view.setOnClickListener(this);

        }
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(context, attrs);
    }

    @Override
    public void onClick(View v) {
        if (dealBusiness != null) {
            dealBusiness.deal(v);
        }
    }

    public interface DealBusiness {
        void deal(View v);
    }
}
