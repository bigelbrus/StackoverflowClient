package com.example.stackoverflowclient;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.stackoverflowclient.data.Question;

import java.util.List;

public class SaveFragment extends Fragment {
    private List mData;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public List getmData(){
        return mData;
    }

    public void setmData(List data) {
        mData = data;
    }
}
