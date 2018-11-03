package com.test.kani.songhistory.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.test.kani.songhistory.R;
import com.test.kani.songhistory.activity.MainActivity;
import com.test.kani.songhistory.utility.FireStoreCallbackListener;
import com.test.kani.songhistory.utility.FireStoreConnectionPool;
import com.test.kani.songhistory.utility.LoadingDialog;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AddHistoryFragment extends Fragment
{
    EditText noEditText, titleEditText, singerEditText, dateEditText, etcEditText;
    Spinner genreSpinner, octaveSpinner, noteSpinner, genderSpinner, changeSpinner, satisfactionSpinner;
    Button addBtn, resetBtn;

    View view;

    private FireStoreCallbackListener fireStoreCallbackListener;
    private LoadingDialog loadingDialog;

    public void setFireStoreCallbackListener(FireStoreCallbackListener listener)
    {
        this.fireStoreCallbackListener = listener;
    }

    public AddHistoryFragment()
    {
        // Required empty public constructor

    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_add_history, container, false);

        bindUI();
        reset();

        return view;
    }

    private void bindUI()
    {
        this.noEditText = view.findViewById(R.id.no_editText);
        this.titleEditText = view.findViewById(R.id.title_editText);
        this.singerEditText = view.findViewById(R.id.singer_editText);
        this.dateEditText = view.findViewById(R.id.date_editText);
        this.etcEditText = view.findViewById(R.id.etc_editText);
        this.genreSpinner = view.findViewById(R.id.genre_spinner);
        this.octaveSpinner = view.findViewById(R.id.octave_spinner);
        this.noteSpinner = view.findViewById(R.id.note_spinner);
        this.genderSpinner = view.findViewById(R.id.gender_spinner);
        this.changeSpinner = view.findViewById(R.id.change_spinner);
        this.satisfactionSpinner = view.findViewById(R.id.satisfaction_spinner);
        this.addBtn = view.findViewById(R.id.add_btn);
        this.resetBtn = view.findViewById(R.id.reset_btn);

        ArrayAdapter<CharSequence> genreAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.genre_list,
                android.R.layout.simple_spinner_item);

        ArrayAdapter<CharSequence> octaveAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.octave_list,
                android.R.layout.simple_spinner_item);

        ArrayAdapter<CharSequence> noteAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.note_list,
                android.R.layout.simple_spinner_item);

        ArrayAdapter<CharSequence> genderAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.gender_list,
                android.R.layout.simple_spinner_item);

        ArrayAdapter<CharSequence> changeAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.change_list,
                android.R.layout.simple_spinner_item);

        ArrayAdapter<CharSequence> satisfactionAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.satisfaction_list,
                android.R.layout.simple_spinner_item);

        genreAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        octaveAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        noteAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        changeAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        satisfactionAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);

        this.genreSpinner.setAdapter(genreAdapter);
        this.octaveSpinner.setAdapter(octaveAdapter);
        this.noteSpinner.setAdapter(noteAdapter);
        this.genderSpinner.setAdapter(genderAdapter);
        this.changeSpinner.setAdapter(changeAdapter);
        this.satisfactionSpinner.setAdapter(satisfactionAdapter);

        this.addBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // 유효성 검사 ㄱ
                if( loadingDialog == null )
                    loadingDialog = new LoadingDialog(getContext());

                loadingDialog.show("Adding...");
                FireStoreConnectionPool.getInstance().insertNoID(fireStoreCallbackListener, makeMap(), "history");
            }
        });

        this.resetBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                reset();
            }
        });

        this.setFireStoreCallbackListener(new FireStoreCallbackListener()
        {
            final int TASK_FAILURE = 0;

            @Override
            public void occurError(int errorCode)
            {
                switch (errorCode)
                {
                    case TASK_FAILURE:
                        Log.d("LoginActivity", "Task is not successful");
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void doNext(boolean isSuccesful, Object obj)
            {
                if( loadingDialog != null )
                    loadingDialog.dismiss();

                if( !isSuccesful )
                {
                    occurError(TASK_FAILURE);
                    return;
                }

                Toast.makeText(getContext(), "Success for adding", Toast.LENGTH_SHORT).show();
                reset();
            }
        });
    }

    private Map<String, Object> makeMap()
    {
        Map<String, Object> map = new HashMap<> ();

        String maxNote = this.noteSpinner.getSelectedItem().toString() + this.octaveSpinner.getSelectedItem().toString();

        map.put("no", this.noEditText.getText().toString().trim());
        map.put("etc", this.etcEditText.getText().toString().trim());
        map.put("id", MainActivity.id);
        map.put("singer", this.singerEditText.getText().toString().trim());
        map.put("title", this.titleEditText.getText().toString().trim());
        map.put("date", this.dateEditText.getText().toString().trim());
        map.put("pitchChange", this.changeSpinner.getSelectedItem().toString());
        map.put("gender", this.genderSpinner.getSelectedItem().toString());
        map.put("satisfaction", this.satisfactionSpinner.getSelectedItem().toString());
        map.put("maxNote", maxNote);
        map.put("genre", this.genreSpinner.getSelectedItem().toString());

        return map;
    }

    private void reset()
    {
        this.noEditText.setText("");
        this.titleEditText.setText("");
        this.singerEditText.setText("");
        this.genreSpinner.setSelection(0);
        this.octaveSpinner.setSelection(0);
        this.noteSpinner.setSelection(0);
        this.genderSpinner.setSelection(0);
        this.changeSpinner.setSelection(0);
        this.satisfactionSpinner.setSelection(0);
        this.dateEditText.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()));
        this.etcEditText.setText("");
    }
}
