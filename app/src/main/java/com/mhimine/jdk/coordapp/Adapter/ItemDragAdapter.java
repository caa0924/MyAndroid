package com.mhimine.jdk.coordapp.Adapter;

import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mhimine.jdk.coordapp.R;
import com.mhimine.jdk.coordapp.Utils.Convert;
import com.mhimine.jdk.coordapp.Coord.Point;

import java.util.List;

/**
 * Created by luoxw on 2016/6/20.
 */
public class ItemDragAdapter extends BaseItemDraggableAdapter<Point, BaseViewHolder> {
    public ItemDragAdapter(List data) {
        super(R.layout.item_draggable_view, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Point point) {
        //helper.setImageResource(R.id.iv_head, R.mipmap.head_img);
        helper.setText(R.id.x_table, Convert.parseString(point.getX()));
        helper.setText(R.id.y_table, Convert.parseString(point.getY()));
        helper.setText(R.id.z_table, Convert.parseString(point.getZ()));
    }
}
