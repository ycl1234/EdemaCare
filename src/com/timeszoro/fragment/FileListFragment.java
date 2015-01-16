package com.timeszoro.fragment;

import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.example.edemacare.R;
import com.timeszoro.mode.FilesLab;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2015/1/15.
 */
public class FileListFragment extends ListFragment{
    private static final String TAG  = "File List Fragment";
    private ArrayList<File> mFiles;
    private static FileListAdapter mFileAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFiles = FilesLab.getFileLab(getActivity()).getFileList();// add the files
        mFileAdapter = new FileListAdapter(mFiles);

        this.setListAdapter(mFileAdapter);
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        ViewHolder vHollder = (ViewHolder) v.getTag();
        vHollder.checkBox.toggle();
        mFileAdapter.isSelected.set(position, vHollder.checkBox.isChecked());

    }

    public void selectAll(){
        for (int i = 0;i < mFileAdapter.getCount();i++){
            mFileAdapter.isSelected.set(i,true);
        }
        mFileAdapter.notifyDataSetInvalidated();
    }

    public void  cancelAll(){
        for (int i = 0;i < mFileAdapter.getCount();i++){
            mFileAdapter.isSelected.set(i,false);
        }
        mFileAdapter.notifyDataSetInvalidated();
    }

    public ArrayList<File> getSendFiles(){
        ArrayList<File> list = new ArrayList<File>();
        for(int i = 0 ;i < mFileAdapter.getCount();i++){
            if(mFileAdapter.isSelected.get(i)){
                list.add(mFileAdapter.fileList.get(i));
            }
        }
        return list;
    }
    public class FileListAdapter extends BaseAdapter {
        List<Boolean> isSelected;
        List<File> fileList;
        HashMap<Integer,View> map = new HashMap<Integer,View>();

        public FileListAdapter(List<File> list){
            fileList = new ArrayList<File>();
            fileList = list;
            isSelected = new ArrayList<Boolean>();
            for(int i = 0 ;i < list.size();i++){
                isSelected.add(false);
            }

        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public Object getItem(int position) {
            return fileList.get(position);
        }

        @Override
        public int getCount() {
            return fileList.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            ViewHolder holder = null;

            if (map.get(position) == null) {
                view = getActivity().getLayoutInflater().inflate(R.layout.file_list_item, null);

                holder = new ViewHolder();
                holder.imageView = (ImageView)view.findViewById(R.id.fileitem_img);
                holder.textView = (TextView)view.findViewById(R.id.fileitem_name);
                holder.checkBox = (CheckBox)view.findViewById(R.id.item_chck);


                final int p = position;
                map.put(position, view);
//                isSelected.set(p,holder.checkBox.isChecked());
//                holder.checkBox.setOnClickListener(new View.OnClickListener() {
//
//                    @Override
//                    public void onClick(View v) {
//                        CheckBox cb = (CheckBox) v;
//                        isSelected.set(p, cb.isChecked());
//                    }
//                });
                view.setTag(holder);
            }else{

                view = map.get(position);
                holder = (ViewHolder)view.getTag();
            }

            holder.imageView.setBackgroundResource(R.drawable.database);
            holder.textView.setText(fileList.get(position).getName());
            holder.checkBox.setChecked(isSelected.get(position));
            return view;
        }
    }

    public FileListAdapter getmFileAdapter(){
        return mFileAdapter;
    }

    static class ViewHolder{
        ImageView imageView;
        TextView textView;
        CheckBox checkBox;

    }
}
