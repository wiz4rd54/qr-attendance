package com.example.qrattendance;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class AcademicCalendarFragment extends Fragment {
    View view;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_academic_calendar, container, false);
        WebView academicCal = view.findViewById(R.id.academic_calendar);

        academicCal.getSettings().setJavaScriptEnabled(true);
        academicCal.getSettings().setLoadWithOverviewMode(true);
        academicCal.getSettings().setUseWideViewPort(true);
        academicCal.getSettings().setBuiltInZoomControls(true);
        academicCal.setWebViewClient(new WebViewClient());

        String pdf = "https://drive.google.com/file/d/1qDRbDaM-iyUjpnqu36qnDCfoOHZi2i4Q/view?usp=sharing/view";
        academicCal.loadUrl(pdf);
        return view;
    }
}