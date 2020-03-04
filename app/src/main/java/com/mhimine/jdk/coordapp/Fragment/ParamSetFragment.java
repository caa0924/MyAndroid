package com.mhimine.jdk.coordapp.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mhimine.jdk.coordapp.Activity.MainActivity;
import com.mhimine.jdk.coordapp.Dialog.SaveTxtDialog;
import com.mhimine.jdk.coordapp.R;
import com.mhimine.jdk.coordapp.Utils.Convert;
import com.mhimine.jdk.coordapp.Coord.TransParaSeven;
import com.mhimine.jdk.coordapp.Coord.CoordTransform;
import com.mhimine.jdk.coordapp.FileManage.FileUtils;
import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * Created by JDK on 2016/8/13.
 */
public class ParamSetFragment extends Fragment implements View.OnClickListener,SaveTxtDialog.ClickExportTxtListener {
    @Bind(R.id.drawerIcon)
    TextView drawerIcon_tv;
    private View v;
    private EditText offsetXBtn;
    private EditText offsetYBtn;
    private EditText offsetZBtn;
    private EditText rotateXBtn;
    private EditText rotateYBtn;
    private EditText rotateZBtn;
    private EditText scaleBtn;
    private Button calcSet;
    private Button calcDel;
    private Spinner spinner;
    private Spinner spinnerTarget;
    private String pattern;
    private Context context;
    private static ParamSetFragment paramSetFragment;
    //private String inputPath = ""; //输入文件路径
    //private String outputPath = ""; //输出文件路径


    public interface collectionDrawerIconListener{
        public void collectionDrawerIcon();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(v!=null){
            ButterKnife.bind(this,v);
            return v;
        }
        v = inflater.inflate(R.layout.fragment_my_paramset, container, false);
        context = v.getContext();
        pattern = getResources().getString(R.string.patternPara);
        setHasOptionsMenu(true);

        Toolbar toolbar =  (Toolbar)v.findViewById(R.id.tool_bar);
        //inflater.setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        ButterKnife.bind(this,v);
        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "iconfont.ttf");
        drawerIcon_tv.setTypeface(typeface);

        offsetXBtn = (EditText) v.findViewById(R.id.offset_X);
        offsetYBtn = (EditText) v.findViewById(R.id.offset_Y);
        offsetZBtn = (EditText) v.findViewById(R.id.offset_Z);
        rotateXBtn = (EditText) v.findViewById(R.id.rotate_X);
        rotateYBtn = (EditText) v.findViewById(R.id.rotate_Y);
        rotateZBtn = (EditText) v.findViewById(R.id.rotate_Z);
        scaleBtn = (EditText) v.findViewById(R.id.scale);
        calcSet = (Button)v.findViewById(R.id.calc_set);
        calcDel = (Button)v.findViewById(R.id.calc_del);
        spinner = (Spinner)v.findViewById(R.id.spinner_source);
        spinnerTarget = (Spinner)v.findViewById(R.id.spinner_target);

        calcSet.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {

                String offsetXStr = offsetXBtn.getText().toString();
                double offsetX = Convert.parseDouble(offsetXStr);

                String offsetYStr = offsetYBtn.getText().toString();
                double offsetY = Convert.parseDouble(offsetYStr);

                String offsetZStr = offsetZBtn.getText().toString();
                double offsetZ = Convert.parseDouble(offsetZStr);

                String rotateXStr = rotateXBtn.getText().toString();
                double rotateX = Convert.parseDouble(rotateXStr);

                String rotateYStr = rotateYBtn.getText().toString();
                double rotateY = Convert.parseDouble(rotateYStr);

                String rotateZStr = rotateZBtn.getText().toString();
                double rotateZ = Convert.parseDouble(rotateZStr);

                String scaleStr = scaleBtn.getText().toString();
                double scale = Convert.parseDouble(scaleStr);

                TransParaSeven transParaSeven = new TransParaSeven(offsetX,offsetY,offsetZ,rotateX,rotateY,rotateZ,scale);
                CoordTransform.SevenParames.put(getCoordType(),transParaSeven);
                readEditText();
            }
        });

        calcDel.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                clearEditText();
                String coordType = getCoordType();
                if (CoordTransform.SevenParames.containsKey(coordType)){
                    CoordTransform.SevenParames.remove(coordType);
                }
            }
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            //当AdapterView中的item被选中的时候执行的方法。
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String spSourceSel =spinner.getSelectedItem().toString();
                Resources res =getResources();
                String[] coords = res.getStringArray(R.array.spinnerCoord);
                List<String> targetSpinner = new ArrayList<>();
                for (String coord : coords) {
                    if (!coord.equals(spSourceSel)){
                        targetSpinner.add(coord);
                    }
                }
                ArrayAdapter<String> adapterTarget = new ArrayAdapter<>(view.getContext(),R.layout.support_simple_spinner_dropdown_item,targetSpinner);
                spinnerTarget.setAdapter(adapterTarget);
                readEditText();
            }

            @Override
            //未选中时的时候执行的方法
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerTarget.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            //当AdapterView中的item被选中的时候执行的方法。
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                readEditText();
            }

            @Override
            //未选中时的时候执行的方法
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //readEditText();

        return v;
    }

    public static ParamSetFragment newInstance(Context context){
        if( paramSetFragment ==null){
            paramSetFragment =new ParamSetFragment();
        }
       // ParamSetFragment paramSetFragment =new ParamSetFragment();
        return paramSetFragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        readEditText();
        drawerIcon_tv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(getActivity() instanceof collectionDrawerIconListener){
            ((collectionDrawerIconListener) getActivity()).collectionDrawerIcon();
        }
    }

    private void clearEditText(){
        offsetXBtn.getText().clear();
        offsetYBtn.getText().clear();
        offsetZBtn.getText().clear();
        rotateXBtn.getText().clear();
        rotateYBtn.getText().clear();
        rotateZBtn.getText().clear();
        scaleBtn.getText().clear();
    }

    private void readEditText(){
        String coordType = getCoordType();
        if (coordType == null) {
            return;
        }

        if (CoordTransform.SevenParames.containsKey(coordType)){
            TransParaSeven transParaSeven = CoordTransform.SevenParames.get(coordType);
            offsetXBtn.setText(Convert.parseString(transParaSeven.getOffsetX(),pattern));
            offsetYBtn.setText(Convert.parseString(transParaSeven.getOffsetY(),pattern));
            offsetZBtn.setText(Convert.parseString(transParaSeven.getOffsetZ(),pattern));
            rotateXBtn.setText(Convert.parseString(transParaSeven.getRotateX(),pattern));
            rotateYBtn.setText(Convert.parseString(transParaSeven.getRotateY(),pattern));
            rotateZBtn.setText(Convert.parseString(transParaSeven.getRotateZ(),pattern));
            scaleBtn.setText(Convert.parseString(transParaSeven.getScale(),pattern));
        }
        else {
            clearEditText();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final int id = item.getItemId();
        if (id == R.id.action_export_coord) {
            SaveTxtDialog dialog = new SaveTxtDialog();
            dialog.setClickListener(this);
            dialog.setTips(FileUtils.ParamFilePath);
            dialog.show(getFragmentManager(),"dialog");
        }else if(id==R.id.action_select_coord){
//            InputStream inputStream = getResources().openRawResource(R.txt.a);
//            Field[] fields = R.raw.class.getDeclaredFields();
//            for(Field field: fields) {
//
//            }
            final String[] items = {"中区参数","北区参数","南区参数"};
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
            alertBuilder.setTitle("请选择转换区域");
            alertBuilder.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int index) {
                    //Toast.makeText(context, items[index], Toast.LENGTH_SHORT).show();
                    InputStream inputStream;// = getResources().openRawResource(R.raw.centreparam);
                    if (items[index].equals("中区参数")) {
                        inputStream = getResources().openRawResource(R.raw.centreparam);
                        selectParam(inputStream);
                    }
                    else if (items[index].equals("北区参数")) {
                        inputStream = getResources().openRawResource(R.raw.northparam);
                        selectParam(inputStream);
                    }
                    else {
                        inputStream = getResources().openRawResource(R.raw.southparam);
                        selectParam(inputStream);
                    }
//                    File txtPath = new File(file, items[index]);
//
//                    importTxt(txtPath.toString());
                }
            });
            alertBuilder.show();
        } else if(id==R.id.action_import_coord) {
            final File file = context.getExternalFilesDir("柠条塔坐标转换/转换参数/");
            List<String> fileNamelist = FileUtils.getFileName(file,new ArrayList<String>(),"");
            String[] fileNames = new String[fileNamelist.size()];
            fileNamelist.toArray(fileNames);
            final String[] items = fileNames;
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
            alertBuilder.setTitle("请选择转换参数文件");
            alertBuilder.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int index) {
                    //Toast.makeText(context, items[index], Toast.LENGTH_SHORT).show();
                    File txtPath = new File(file, items[index]);
                    importTxt(txtPath.toString());
                }
            });
//            alertBuilder.
            alertBuilder.show();
        } else if(id==R.id.action_file_manage) {
            DialogFileMangementFragment dialog = new DialogFileMangementFragment();
            dialog.show(getFragmentManager(),"param");
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.trans_para_set_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private String getCoordType(){
        String spSouStr = spinner.getSelectedItem().toString();
        if (spinnerTarget.getSelectedItem() == null){
            return null;
        }
        String spTarStr = spinnerTarget.getSelectedItem().toString();
        String coordType = spSouStr + ";" + spTarStr;
        return coordType;
    }

    @Override
    public void onConfirmExport(String txtName, String tag) {
        File sdPath = context.getExternalFilesDir("柠条塔坐标转换/转换参数/");
        File file = new File(sdPath, txtName + ".txt");//Environment.getExternalStorageDirectory();
        final String filePath = file.toString();
        if (file.exists()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);//内部类
            builder.setTitle("提示");
            builder.setMessage("已存在该文件，是否覆盖?");
            //确定按钮
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //确定删除的代码
                    saveTxt(filePath);
                }
            });
            //点取消按钮
            builder.setNegativeButton("取消", null);
            builder.show();
        }
        else {
            saveTxt(filePath);
        }
    }

    private void saveTxt(String filePathName) {
        FileUtils.CreateFile(filePathName);
        FileUtils.saveFile(filePathName, FileUtils.saveParaFile(this.context));
        Toast.makeText(this.context.getApplicationContext(),"导出成功！",Toast.LENGTH_SHORT).show();
    }

    private void selectParam(InputStream inputStream){
//        String inputStr = FileUtils.readFile(txtName);
        String inputStr = FileUtils.getStringByInputStream(inputStream);
        Map<String,TransParaSeven> SevenParames = FileUtils.restoreMapFromFile(this.context,inputStr);
        if (SevenParames.size() > 0) {
            CoordTransform.SevenParames = SevenParames;
            readEditText();
            Toast.makeText(this.context.getApplicationContext(),"导入成功！",Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this.context.getApplicationContext(),"参数文件为空或者格式不正确！",Toast.LENGTH_SHORT).show();
        }
    }

    private void importTxt(String txtName) {
        String inputStr = FileUtils.readFile(txtName);
        Map<String,TransParaSeven> SevenParames = FileUtils.restoreMapFromFile(this.context,inputStr);
        if (SevenParames.size() > 0) {
            CoordTransform.SevenParames = SevenParames;
            readEditText();
            Toast.makeText(this.context.getApplicationContext(),"导入成功！",Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this.context.getApplicationContext(),"参数文件为空或者格式不正确！",Toast.LENGTH_SHORT).show();
        }
    }
}
