package com.mhimine.jdk.coordapp.Fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DialogTitle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mhimine.jdk.coordapp.Adapter.FilesItemDragAdapter;
import com.mhimine.jdk.coordapp.FileManage.FileInfo;
import com.mhimine.jdk.coordapp.FileManage.FileUtils;
import com.mhimine.jdk.coordapp.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DialogFileMangementFragment extends DialogFragment implements View.OnClickListener,FilesItemDragAdapter.FilesItemViewHolerClicks {

    private DialogTitle dialogTitle;
//    private RecyclerView filesRecyclerView;
    FilesItemDragAdapter filesAdapter;
    private FileInfo fileInfo;
    Context context;



    public void setPoint(FileInfo fileInfo) {
        this.fileInfo = fileInfo;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        return inflater.inflate(R.layout.dialog_file_management, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getContext();
//        EditText fileNmaeEt = view.findViewById(R.id.file_name);
        Button mCancel = view.findViewById(R.id.file_cancel);
//        Button mConfirm = view.findViewById(R.id.file_ok);
        dialogTitle = view.findViewById(R.id.title);

        //获取布局参数
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        view.findViewById(R.id.file_dialog_layout).setMinimumWidth(outMetrics.widthPixels/2);


        RecyclerView filesRecyclerView = view.findViewById(R.id.files_recyclerView);
        filesRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        List<FileInfo> filesData;
        if (getTag().equals("param")) {
            filesData = addFilesAdapter("转换参数");
        } else {
            filesData = addFilesAdapter("坐标文件");
        }
        filesAdapter = new FilesItemDragAdapter(filesData);
//        ItemDragAndSwipeCallback filesItemDragAndSwipeCallback = new ItemDragAndSwipeCallback(filesAdapter);
//        ItemTouchHelper filesItemTouchHelper = new ItemTouchHelper(filesItemDragAndSwipeCallback);
//        filesItemTouchHelper.attachToRecyclerView(filesRecyclerView);
//        filesItemDragAndSwipeCallback.setSwipeMoveFlags(ItemTouchHelper.START | ItemTouchHelper.END);
        filesAdapter.enableSwipeItem();
//        filesAdapter.enableDragItem(filesItemTouchHelper);
        filesRecyclerView.setAdapter(filesAdapter);

        filesAdapter.setFilesItemViewHolerClicks(this);



//        if (fileInfo != null) {
//            fileNmaeEt.setText(fileInfo.getFileName());
//        }

        mCancel.setOnClickListener(this);
//        mConfirm.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.file_ok:
//                {
//
//                }
//                break;
            case R.id.file_cancel:
                dismiss();
                break;
            default:
                break;
        }
    }

    private List<FileInfo> addFilesAdapter(String type) {
        List<FileInfo> fileNamelist = new ArrayList<>();
        File file = context.getExternalFilesDir("柠条塔坐标转换/" + type + "/");
        if (file.exists()) {
            fileNamelist = FileUtils.getFileInfos(file,new ArrayList<FileInfo>(),"");
        }
        return fileNamelist;
    }

    @Override
    public void deleteClick(int position) {
        final int pos = position;
        FileInfo fileInfo = filesAdapter.getItem(pos);
        final String path = fileInfo.getFileFullPath();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);//内部类
        builder.setTitle("提示");
        builder.setMessage("是否确定删除文件“" + fileInfo.getFileName() + "”?");
        //确定按钮
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                //确定删除的代码
                FileInfo fileInfo = filesAdapter.getItem(pos);
                File file = new File(path);
                if (FileUtils.delFile(file)) {
                    filesAdapter.remove(pos);
                }
                else {
                    Toast.makeText(context.getApplicationContext(), "删除失败，请重新尝试！", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //点取消按钮
        builder.setNegativeButton("取消", null);
        builder.show();

    }
}
