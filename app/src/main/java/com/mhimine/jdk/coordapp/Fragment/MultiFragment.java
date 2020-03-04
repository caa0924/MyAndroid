package com.mhimine.jdk.coordapp.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback;
import com.mhimine.jdk.coordapp.Dialog.SaveTxtDialog;
import com.mhimine.jdk.coordapp.R;
import com.mhimine.jdk.coordapp.Utils.Convert;
import com.mhimine.jdk.coordapp.Coord.Point;
import com.mhimine.jdk.coordapp.Coord.TransParaSeven;
import com.mhimine.jdk.coordapp.Adapter.ItemDragAdapter;
import com.mhimine.jdk.coordapp.Coord.CoordTransform;
import com.mhimine.jdk.coordapp.FileManage.FileUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by JDK on 2016/8/4.
 */
public class MultiFragment extends Fragment implements CoordEditDialog.ClickListener, SaveTxtDialog.ClickExportTxtListener {

    private View view;
    private Spinner spinnerSource;
    private Spinner spinnerTarget;
    private Button transButton;
    private Button clearBtn;
    private ImageButton addCoordBtn;
    private RecyclerView sourceRecyclerView;
    private RecyclerView targetRecyclerView;

    private ImageButton importSourceBtn;
    private ImageButton exportSourceBtn;
    private ImageButton exportTargetBtn;

    private ItemDragAdapter sourceAdapter;
    private ItemDragAdapter targetAdapter;

    private Context context;

    public static MultiFragment newInstance(Context context){
        MultiFragment multiFragment=new MultiFragment();
        return multiFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_watch_multi, container, false);
        context = view.getContext();
        spinnerSource = (Spinner)view.findViewById(R.id.multi_point_source);
        spinnerTarget = (Spinner)view.findViewById(R.id.multi_point_target);
        transButton = (Button) view.findViewById(R.id.multi_transBtn);
       // clearBtn = (Button) view.findViewById(R.id.multi_clearBtn);
        addCoordBtn = (ImageButton) view.findViewById(R.id.add_coord);
        sourceRecyclerView = (RecyclerView) view.findViewById(R.id.multi_point_recyclerView);
        targetRecyclerView = (RecyclerView) view.findViewById(R.id.rv_list);
        importSourceBtn = (ImageButton) view.findViewById(R.id.import_point_source);
        exportSourceBtn = (ImageButton) view.findViewById(R.id.export_point_source);
        exportTargetBtn = (ImageButton) view.findViewById(R.id.export_point_target);


        sourceRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        List<Point> sourceData = new ArrayList<>();
        sourceAdapter = new ItemDragAdapter(sourceData);
        ItemDragAndSwipeCallback sourceItemDragAndSwipeCallback = new ItemDragAndSwipeCallback(sourceAdapter);
        ItemTouchHelper sourceItemTouchHelper = new ItemTouchHelper(sourceItemDragAndSwipeCallback);
        sourceItemTouchHelper.attachToRecyclerView(sourceRecyclerView);
        sourceItemDragAndSwipeCallback.setSwipeMoveFlags(ItemTouchHelper.START | ItemTouchHelper.END);
        sourceAdapter.enableSwipeItem();
        sourceAdapter.enableDragItem(sourceItemTouchHelper);
        sourceRecyclerView.setAdapter(sourceAdapter);
        sourceAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                Point point = sourceAdapter.getItem(position);
                CoordEditDialog dialog = new CoordEditDialog();
                dialog.setPoint(point);
                dialog.setOperateRowIndex(position);
                dialog.setClickListener(MultiFragment.this);
                dialog.show(getFragmentManager(),"editPoint");
            }
        });

        targetRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        List<String> mData = new ArrayList<>();
        targetAdapter = new ItemDragAdapter(mData);
        //ItemDragAndSwipeCallback mItemDragAndSwipeCallback = new ItemDragAndSwipeCallback(targetAdapter);
        //ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(mItemDragAndSwipeCallback);
        //mItemTouchHelper.attachToRecyclerView(targetRecyclerView);
        //targetAdapter.enableDragItem(mItemTouchHelper);
        targetRecyclerView.setAdapter(targetAdapter);

        targetAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Point point = targetAdapter.getItem(position);
                CoordEditDialog dialog = new CoordEditDialog();
                dialog.setPoint(point);
                //dialog.setClickListener(AndroidFragment.this);
                dialog.show(getFragmentManager(),"SeePoint");
            }
        });
        targetAdapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
                Toast.makeText(context.getApplicationContext(), "onItemLongClick" + position, Toast.LENGTH_SHORT).show();
                return false;
            }
        });


        addCoordBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                CoordEditDialog dialog = new CoordEditDialog();
                dialog.setClickListener(MultiFragment.this);
                dialog.show(getFragmentManager(),"dialogCoord");
            }
        });

        transButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                String curCoordTransType = getCoordType();
                if (curCoordTransType == null) return;
                if (CoordTransform.SevenParames.containsKey(curCoordTransType)){
                    List<Point> sourceData = sourceAdapter.getData();
                    List<Point> targetData = new ArrayList<>();
                    int i = 0;
                    for (Point point : sourceData) {
                        i++;
                        if (!CoordTransform.isRange(point)) {
                            Toast.makeText(context.getApplicationContext(),"第" + i + "个点超出该矿区范围，无法转换！",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        TransParaSeven transParaSeven = CoordTransform.SevenParames.get(curCoordTransType);
                        Point newPoint = CoordTransform.BolsaSevenParameters(transParaSeven, point);
                        targetData.add(newPoint);
                    }
                    targetAdapter.setNewData(targetData);
                }
                else {
                    Toast.makeText(context.getApplicationContext(),"未设置该转换的转换参数，请先设置转换参数！",Toast.LENGTH_SHORT).show();
                }
            }
        });

//        clearBtn.setOnClickListener(new View.OnClickListener(){
//            public void onClick(View v) {
//                sourceAdapter.setNewData(new ArrayList<Point>());
//                targetAdapter.setNewData(new ArrayList<Point>());
//            }
//        });

        importSourceBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                final File file = context.getExternalFilesDir("柠条塔坐标转换/坐标文件/");
                List<String> fileNamelist = FileUtils.getFileName(file,new ArrayList<String>(),"");
                String[] fileNames = new String[fileNamelist.size()];
                fileNamelist.toArray(fileNames);
                final String[] items = fileNames;
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                alertBuilder.setTitle("请选择坐标文件");
                alertBuilder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int index) {
                        //Toast.makeText(context, items[index], Toast.LENGTH_SHORT).show();
                        File txtPath = new File(file, items[index]);
                        importTxt(txtPath);
                    }
                });
                alertBuilder.show();
            }
        });

        final SaveTxtDialog.ClickExportTxtListener _this = this;
        exportSourceBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                SaveTxtDialog dialog = new SaveTxtDialog();
                dialog.setClickListener(_this);
                dialog.setTips(FileUtils.CoordFilePath);
                dialog.show(getFragmentManager(),"source");
            }
        });

        exportTargetBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                SaveTxtDialog dialog = new SaveTxtDialog();
                dialog.setClickListener(_this);
                dialog.setTips(FileUtils.CoordFilePath);
                dialog.show(getFragmentManager(),"target");
            }
        });

        spinnerSource.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            //当AdapterView中的item被选中的时候执行的方法。
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String spSourceSel =spinnerSource.getSelectedItem().toString();
                Resources res =getResources();
                String[] coords = res.getStringArray(R.array.spinnerCoord);
                List<String> targetSpinner = new ArrayList<>();
                for (String coord : coords) {
                    if (!coord.equals(spSourceSel)){
                        targetSpinner.add(coord);
                    }
                }
                ArrayAdapter<String> adapterTarget = new ArrayAdapter<>(context,R.layout.support_simple_spinner_dropdown_item,targetSpinner);
                spinnerTarget.setAdapter(adapterTarget);
            }

            @Override
            //未选中时的时候执行的方法
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(context.getApplicationContext(),"warning",Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    @Override
    public void onConfirmAdd(String name, double x, double y, double z) {
        Point point = new Point(name, x, y, z);
        sourceAdapter.addData(point);
    }

    @Override
    public void onConfirmEdit(String name, double x, double y, double z,int operateRowIndex) {
        Point point = new Point(name,x, y, z);
        sourceAdapter.setData(operateRowIndex, point);
    }

    private void importPoint(String importPath) {
        if (importPath.equals("")){
            Toast.makeText(context.getApplicationContext(),"请选择选择文件！",Toast.LENGTH_SHORT).show();
            return;
        }
        List<Point> points = FileUtils.getPoints(importPath,context);
        if (points == null) {
            return;
        }
        sourceAdapter.setNewData(points);
    }
    private void exportPoint(String exportPath, ItemDragAdapter adapter) {
        if (exportPath.equals("")){
            Toast.makeText(context.getApplicationContext(),"请选择选择文件！",Toast.LENGTH_SHORT).show();
            return;
        }
        List<Point> points = adapter.getData();
        if (points == null) {
            return;
        }

        if (FileUtils.saveTargetPoint(exportPath, points)){
            Toast.makeText(context.getApplicationContext(),"导出成功！",Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(context.getApplicationContext(),"导出失败！",Toast.LENGTH_SHORT).show();
        }
    }

    private String getCoordType(){
        String spSouStr = spinnerSource.getSelectedItem().toString();
        if (spinnerTarget.getSelectedItem() == null){
            return null;
        }
        String spTarStr = spinnerTarget.getSelectedItem().toString();
        String coordType = spSouStr + ";" + spTarStr;
        return coordType;
    }

    private void importTxt(File file){
        if (file.exists()) {
            importPoint(file.toString());
            Toast.makeText(context.getApplicationContext(),"导入成功！",Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(context.getApplicationContext(),"文件不存在！",Toast.LENGTH_SHORT).show();
        }
    }

    private void exportTxt(File file, String tag){
//        File file = new File(path);
        FileUtils.CreateFile(file.toString());
        if ("source".equals(tag)){
            exportPoint(file.toString(), sourceAdapter);
        }
        else {
            exportPoint(file.toString(), targetAdapter);
        }
        Toast.makeText(this.context.getApplicationContext(),"导出成功！",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConfirmExport(String txtName, String tag) {
        File sdPath = context.getExternalFilesDir("柠条塔坐标转换/坐标文件/");
        final File file = new File(sdPath, txtName + ".txt");
        //final String filePath = file.toString();
        final String _tag = tag;
        if (file.exists()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);//内部类
            builder.setTitle("提示");
            builder.setMessage("已存在该文件，是否覆盖?");
            //确定按钮

            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //确定删除的代码
                    exportTxt(file, _tag);
                }
            });
            //点取消按钮
            builder.setNegativeButton("取消", null);
            builder.show();
        }
        else {
            exportTxt(file, tag);
        }
    }
}
