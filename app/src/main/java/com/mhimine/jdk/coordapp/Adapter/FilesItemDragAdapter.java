package com.mhimine.jdk.coordapp.Adapter;

import android.view.View;
import android.widget.ImageButton;
import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mhimine.jdk.coordapp.FileManage.FileInfo;
import com.mhimine.jdk.coordapp.R;

import java.util.List;

public class FilesItemDragAdapter  extends BaseItemDraggableAdapter<FileInfo, BaseViewHolder> {


    private FilesItemViewHolerClicks filesItemViewHolerClicks;

    public void setFilesItemViewHolerClicks(FilesItemViewHolerClicks filesItemViewHolerClicks) {
        this.filesItemViewHolerClicks = filesItemViewHolerClicks;
    }


    public FilesItemDragAdapter(List data) {
        super(R.layout.item_draggable_files_list, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, FileInfo fileInfo) {
        baseViewHolder.setText(R.id.file_name, fileInfo.getFileName());
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        final int pos = position;
        //绑定view的方法
        if (filesItemViewHolerClicks != null) {
            holder.getView(R.id.delete_file).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    filesItemViewHolerClicks.deleteClick(pos);
                }
            });
        }
    }
    public interface FilesItemViewHolerClicks {
        void deleteClick(int position);
    }
}
