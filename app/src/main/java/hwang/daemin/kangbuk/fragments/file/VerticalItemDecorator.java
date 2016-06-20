package hwang.daemin.kangbuk.fragments.file;

/**
 * Created by user on 2016-06-20.
 */
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * @author msahakyan
 */
public class VerticalItemDecorator extends RecyclerView.ItemDecoration {

    private final int mVerticalSpaceWidth;

    public VerticalItemDecorator(int verticalSpaceWidth) {
        this.mVerticalSpaceWidth = verticalSpaceWidth;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        outRect.top = outRect.bottom = mVerticalSpaceWidth;
    }
}