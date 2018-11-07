package com.test.kani.songhistory.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.test.kani.songhistory.R;
import com.test.kani.songhistory.activity.MainActivity;
import com.test.kani.songhistory.adapter.HistoryListAdapter;
import com.test.kani.songhistory.utility.FireStoreCallbackListener;
import com.test.kani.songhistory.utility.FireStoreConnectionPool;
import com.test.kani.songhistory.utility.HistoryDetailDialog;
import com.test.kani.songhistory.utility.LoadingDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ViewHistoryFragment extends Fragment
{
    Spinner searchSpinner, genreSpinner, octaveSpinner1, octaveSpinner2, noteSpinner1, noteSpinner2, genderSpinner,
            pitchChangeSpinner1, pitchChangeSpinner2, satisfactionSpinner1, satisfactionSpinner2;
    EditText searchEditText, dateEditText1, dateEditText2;
    ImageButton searchImgBtn;
    TextView resultTextView, splitterTextView;
    ListView historyListView;

    View view;

    private ArrayList<HashMap<String, Object>> historyList;
    private ArrayList<HashMap<String, Object>> subHistoryList;
    private HistoryListAdapter adapter;
    private FireStoreCallbackListener fireStoreCallbackListener;
    private LoadingDialog loadingDialog;
    private HistoryDetailDialog historyDetailDialog;

    public void setFireStoreCallbackListener(FireStoreCallbackListener listener)
    {
        this.fireStoreCallbackListener = listener;
    }

    public ViewHistoryFragment()
    {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment

        this.view = inflater.inflate(R.layout.fragment_view_history, container, false);

        this.init();

        return view;
    }

    private void init()
    {
        this.searchEditText = this.view.findViewById(R.id.search_editText);
        this.searchImgBtn = this.view.findViewById(R.id.search_imgBtn);
        this.splitterTextView = this.view.findViewById(R.id.splitter_textView);
        this.searchSpinner = this.view.findViewById(R.id.search_spinner);
        this.genderSpinner = this.view.findViewById(R.id.gender_spinner);
        this.genreSpinner = this.view.findViewById(R.id.genre_spinner);
        this.satisfactionSpinner1 = this.view.findViewById(R.id.satisfaction_spinner1);
        this.satisfactionSpinner2 = this.view.findViewById(R.id.satisfaction_spinner2);
        this.octaveSpinner1 = this.view.findViewById(R.id.octave_spinner1);
        this.octaveSpinner2 = this.view.findViewById(R.id.octave_spinner2);
        this.noteSpinner1 = this.view.findViewById(R.id.note_spinner1);
        this.noteSpinner2 = this.view.findViewById(R.id.note_spinner2);
        this.pitchChangeSpinner1= this.view.findViewById(R.id.pitchChange_spinner1);
        this.pitchChangeSpinner2 = this.view.findViewById(R.id.pitchChange_spinner2);
        this.dateEditText1 = this.view.findViewById(R.id.date_editText1);
        this.dateEditText2 = this.view.findViewById(R.id.date_editText2);

        ArrayAdapter<CharSequence> searchAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.search_list,
                android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> genderAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.gender_list,
                android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> genreAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.genre_list,
                android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> satisfactionAdapter1 = ArrayAdapter.createFromResource(getActivity(), R.array.satisfaction_list,
                android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> satisfactionAdapter2 = ArrayAdapter.createFromResource(getActivity(), R.array.satisfaction_list,
                android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> octaveAdapter1 = ArrayAdapter.createFromResource(getActivity(), R.array.octave_list,
                android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> octaveAdapter2 = ArrayAdapter.createFromResource(getActivity(), R.array.octave_list,
                android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> noteAdapter1 = ArrayAdapter.createFromResource(getActivity(), R.array.note_list,
                android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> noteAdapter2 = ArrayAdapter.createFromResource(getActivity(), R.array.note_list,
                android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> pitchChangeAdapter1 = ArrayAdapter.createFromResource(getActivity(), R.array.change_list,
                android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> pitchChangeAdapter2 = ArrayAdapter.createFromResource(getActivity(), R.array.change_list,
                android.R.layout.simple_spinner_item);

        searchAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        genreAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        satisfactionAdapter1.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        satisfactionAdapter2.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        octaveAdapter1.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        octaveAdapter2.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        noteAdapter1.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        noteAdapter2.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        pitchChangeAdapter1.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        pitchChangeAdapter2.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);

        this.searchSpinner.setAdapter(searchAdapter);
        this.genderSpinner.setAdapter(genderAdapter);
        this.genreSpinner.setAdapter(genreAdapter);
        this.satisfactionSpinner1.setAdapter(satisfactionAdapter1);
        this.satisfactionSpinner2.setAdapter(satisfactionAdapter2);
        this.octaveSpinner1.setAdapter(octaveAdapter1);
        this.octaveSpinner2.setAdapter(octaveAdapter2);
        this.noteSpinner1.setAdapter(noteAdapter1);
        this.noteSpinner2.setAdapter(noteAdapter2);
        this.pitchChangeSpinner1.setAdapter(pitchChangeAdapter1);
        this.pitchChangeSpinner2.setAdapter(pitchChangeAdapter2);

        this.searchSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                if( "번호".equals(searchSpinner.getSelectedItem().toString().trim()) )
                {
                    allSearchCategoryViewGone();
                    searchEditText.setVisibility(EditText.VISIBLE);
                    searchEditText.setText("");
                }
                else if( "제목".equals(searchSpinner.getSelectedItem().toString().trim()) )
                {
                    allSearchCategoryViewGone();
                    searchEditText.setVisibility(EditText.VISIBLE);
                    searchEditText.setText("");
                }
                else if( "가수".equals(searchSpinner.getSelectedItem().toString().trim()) )
                {
                    allSearchCategoryViewGone();
                    searchEditText.setVisibility(EditText.VISIBLE);
                    searchEditText.setText("");
                }
                else if( "성별".equals(searchSpinner.getSelectedItem().toString().trim()) )
                {
                    allSearchCategoryViewGone();
                    genderSpinner.setVisibility(EditText.VISIBLE);
                    genderSpinner.setSelection(0);
                }
                else if( "장르".equals(searchSpinner.getSelectedItem().toString().trim()) )
                {
                    allSearchCategoryViewGone();
                    genreSpinner.setVisibility(EditText.VISIBLE);
                    genreSpinner.setSelection(0);
                }
                else if( "최고음".equals(searchSpinner.getSelectedItem().toString().trim()) )
                {
                    allSearchCategoryViewGone();
                    octaveSpinner1.setVisibility(EditText.VISIBLE);
                    octaveSpinner1.setSelection(0);
                    octaveSpinner2.setVisibility(EditText.VISIBLE);
                    octaveSpinner2.setSelection(0);
                    noteSpinner1.setVisibility(EditText.VISIBLE);
                    noteSpinner1.setSelection(0);
                    noteSpinner2.setVisibility(EditText.VISIBLE);
                    noteSpinner2.setSelection(0);
                    splitterTextView.setVisibility(TextView.VISIBLE);
                }
                else if( "키변조".equals(searchSpinner.getSelectedItem().toString().trim()) )
                {
                    allSearchCategoryViewGone();
                    pitchChangeSpinner1.setVisibility(EditText.VISIBLE);
                    pitchChangeSpinner1.setSelection(0);
                    pitchChangeSpinner2.setVisibility(EditText.VISIBLE);
                    pitchChangeSpinner2.setSelection(0);
                    splitterTextView.setVisibility(TextView.VISIBLE);
                }
                else if( "날짜".equals(searchSpinner.getSelectedItem().toString().trim()) )
                {
                    allSearchCategoryViewGone();
                    dateEditText1.setVisibility(EditText.VISIBLE);
                    dateEditText1.setText("");
                    dateEditText2.setVisibility(EditText.VISIBLE);
                    dateEditText2.setText("");
                    splitterTextView.setVisibility(TextView.VISIBLE);
                }
                else if( "만족도".equals(searchSpinner.getSelectedItem().toString().trim()) )
                {
                    allSearchCategoryViewGone();
                    satisfactionSpinner1.setVisibility(EditText.VISIBLE);
                    satisfactionSpinner1.setSelection(0);
                    satisfactionSpinner2.setVisibility(EditText.VISIBLE);
                    satisfactionSpinner2.setSelection(0);
                    splitterTextView.setVisibility(TextView.VISIBLE);
                }
                else if( "기타".equals(searchSpinner.getSelectedItem().toString().trim()) )
                {
                    allSearchCategoryViewGone();
                    searchEditText.setVisibility(EditText.VISIBLE);
                    searchEditText.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        this.searchImgBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if( !loadingDialog.isShowing() )
                    loadingDialog.show("Searching...");

                subHistoryList.clear();

                for (HashMap<String, Object> map : historyList)
                {
                    if ("번호".equals(searchSpinner.getSelectedItem().toString().trim()))
                    {
                        String filter = trimAll(searchEditText.getText().toString()).toLowerCase();

                        if( trimAll(((String) map.get("no"))).toLowerCase().equals(filter) )
                            subHistoryList.add(map);

//                    FireStoreConnectionPool.getInstance().selectEqual(fireStoreCallbackListener, "history", "id", MainActivity.id,
//                            "no", filter, "date");
                    }
                    else if ("제목".equals(searchSpinner.getSelectedItem().toString().trim()))
                    {
                        String filter = trimAll(searchEditText.getText().toString()).toLowerCase();

                        if( trimAll(((String) map.get("title"))).toLowerCase().contains(filter) )
                            subHistoryList.add(map);

//                    FireStoreConnectionPool.getInstance().selectGreaterEqual(fireStoreCallbackListener, "history", "id", MainActivity.id,
//                            "title", filter, "date");
                    }
                    else if ("가수".equals(searchSpinner.getSelectedItem().toString().trim()))
                    {
                        String filter = trimAll(searchEditText.getText().toString()).toLowerCase();

                        if( trimAll(((String) map.get("singer"))).toLowerCase().contains(filter) )
                            subHistoryList.add(map);

//                    FireStoreConnectionPool.getInstance().selectGreaterEqual(fireStoreCallbackListener, "history", "id", MainActivity.id,
//                            "singer", filter, "date");
                    }
                    else if ("성별".equals(searchSpinner.getSelectedItem().toString().trim()))
                    {
                        String filter = (genderSpinner.getSelectedItemPosition() > 0) ?
                                trimAll(genderSpinner.getSelectedItem().toString()).toLowerCase(): "";

                        if( trimAll(((String) map.get("gender"))).toLowerCase().equals(filter) )
                            subHistoryList.add(map);

//                    FireStoreConnectionPool.getInstance().selectEqual(fireStoreCallbackListener, "history", "id", MainActivity.id,
//                            "gender", filter, "date");
                    }
                    else if ("장르".equals(searchSpinner.getSelectedItem().toString().trim()))
                    {
                        String filter = (genreSpinner.getSelectedItemPosition() > 0) ?
                                trimAll(genreSpinner.getSelectedItem().toString()).toLowerCase(): "";

                        if( trimAll(((String) map.get("genre"))).toLowerCase().equals(filter) )
                            subHistoryList.add(map);

//                    FireStoreConnectionPool.getInstance().selectEqual(fireStoreCallbackListener, "history", "id", MainActivity.id,
//                            "genre", filter, "date");
                    }
                    else if ("최고음".equals(searchSpinner.getSelectedItem().toString().trim()))
                    {
                        String filter1 = (octaveSpinner1.getSelectedItemPosition() > 0) ?
                                trimAll(octaveSpinner1.getSelectedItem().toString()).toLowerCase() : "2";
                        String filter2 = (octaveSpinner2.getSelectedItemPosition() > 0) ?
                                trimAll(octaveSpinner2.getSelectedItem().toString()).toLowerCase() : "7";

                        filter1 += (noteSpinner1.getSelectedItemPosition() > 0) ?
                                trimAll(noteSpinner1.getSelectedItem().toString()).toLowerCase() : "c";
                        filter2 += (noteSpinner2.getSelectedItemPosition() > 0) ?
                                trimAll(noteSpinner2.getSelectedItem().toString()).toLowerCase() : "b";

                        double filterValue1 = getMaxNoteValue(filter1);
                        double filterValue2 = getMaxNoteValue(filter2);

                        if( getMaxNoteValue(trimAll(((String) map.get("maxNote"))).toLowerCase()) >= filterValue1 &&
                                getMaxNoteValue(trimAll(((String) map.get("maxNote"))).toLowerCase()) <= filterValue2 )
                            subHistoryList.add(map);

//                    FireStoreConnectionPool.getInstance().selectGreaterLessEqual(fireStoreCallbackListener, "history", "id", MainActivity.id,
//                            "maxNote", filter1, "maxNote", filter2, "date");
                    }
                    else if ("키변조".equals(searchSpinner.getSelectedItem().toString().trim()))
                    {
                        int filter1 = (pitchChangeSpinner1.getSelectedItemPosition() > 0) ?
                                Integer.parseInt(trimAll(pitchChangeSpinner1.getSelectedItem().toString()).toLowerCase()) : -8;
                        int filter2 = (pitchChangeSpinner2.getSelectedItemPosition() > 0) ?
                                Integer.parseInt(trimAll(pitchChangeSpinner2.getSelectedItem().toString()).toLowerCase()) : 8;

                        if( Integer.parseInt(trimAll(((String) map.get("pitchChange"))).toLowerCase()) >= filter1 &&
                                Integer.parseInt(trimAll(((String) map.get("pitchChange"))).toLowerCase()) <= filter2 )
                            subHistoryList.add(map);

//                    FireStoreConnectionPool.getInstance().selectGreaterLessEqual(fireStoreCallbackListener, "history", "id", MainActivity.id,
//                            "pitchChange", filter1, "pitchChange", filter2, "date");
                    }
                    else if ("날짜".equals(searchSpinner.getSelectedItem().toString().trim()))
                    {
                        String filter1 = (dateEditText1.getText().toString().trim().length() > 0)?
                                trimAll(dateEditText1.getText().toString()).toLowerCase(): "0000-00-00 00:00";
                        String filter2 = (dateEditText2.getText().toString().trim().length() > 0)?
                                trimAll(dateEditText2.getText().toString()).toLowerCase():
                                new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());

                        filter1 = toDateString(filter1, 0);
                        filter2 = toDateString(filter2, 9999);
                        String date = (String) map.get("date");

                        if( filter1 == null || filter2 == null )
                        {
                            loadingDialog.dismiss();
                            Toast.makeText(getContext(), "양식에 맞게 다시입력해주세요.", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if( date.compareTo(filter1) >= 0 && date.compareTo(filter2) <= 0  )
                            subHistoryList.add(map);

//                    FireStoreConnectionPool.getInstance().selectGreaterLessEqual(fireStoreCallbackListener, "history", "id", MainActivity.id,
//                            "date", filter1, "date", filter2, "date");
                    }
                    else if ("만족도".equals(searchSpinner.getSelectedItem().toString().trim()))
                    {
                        int filter1 = (satisfactionSpinner1.getSelectedItemPosition() > 0) ?
                                Integer.parseInt(trimAll(satisfactionSpinner1.getSelectedItem().toString()).toLowerCase()) : 0;
                        int filter2 = (satisfactionSpinner2.getSelectedItemPosition() > 0) ?
                                Integer.parseInt(trimAll(satisfactionSpinner2.getSelectedItem().toString()).toLowerCase()) : 100;

                        if( Integer.parseInt(trimAll(((String) map.get("satisfaction"))).toLowerCase()) >= filter1 &&
                                Integer.parseInt(trimAll(((String) map.get("satisfaction"))).toLowerCase()) <= filter2 )
                            subHistoryList.add(map);

//                    FireStoreConnectionPool.getInstance().selectGreaterLessEqual(fireStoreCallbackListener, "history", "id", MainActivity.id,
//                            "satisfaction", filter1, "satisfaction", filter2, "date");
                    }
                    else if ("기타".equals(searchSpinner.getSelectedItem().toString().trim()))
                    {
                        String filter = trimAll(searchEditText.getText().toString()).toLowerCase();

                        if( trimAll(((String) map.get("etc"))).toLowerCase().contains(filter) )
                            subHistoryList.add(map);

//                    FireStoreConnectionPool.getInstance().selectGreaterEqual(fireStoreCallbackListener, "history", "id", MainActivity.id,
//                            "etc", filter, "date");
                    }
                }

                adapter.notifyDataSetChanged();
//                adapter = new HistoryListAdapter(getActivity(), R.layout.history_item, subHistoryList);
//                historyListView.setAdapter(adapter);
                resultTextView.setText("검색 결과 " + subHistoryList.size() + "건");
                loadingDialog.dismiss();
            }
        });

        if( this.loadingDialog == null )
            this.loadingDialog = new LoadingDialog(getContext());

        this.loadingDialog.show("Loading HistoryList...");

        this.setFireStoreCallbackListener(new FireStoreCallbackListener()
        {
            final int TASK_FAILURE = 1;

            @Override
            public void occurError(int errorCode)
            {
                switch (errorCode)
                {
                    case TASK_FAILURE:
                        Log.d("ReportFragment", "Task is not successful");
                        break;
                    default:
                        break;
                }

                if( loadingDialog.isShowing() )
                    loadingDialog.dismiss();
            }

            @Override
            public void doNext(boolean isSuccesful, Object obj)
            {
                if (loadingDialog != null && loadingDialog.isShowing())
                    loadingDialog.dismiss();

                if (!isSuccesful)
                {
                    occurError(TASK_FAILURE);
                    return;
                }

                subHistoryList = new ArrayList<> ();

                if (obj == null)
                {
                    Log.d("HistoryFragment", "History is not found");
                    historyList = new ArrayList<>();
                }
                else if (obj != null && !(obj instanceof Boolean) && !(obj instanceof String))
                {
                    historyList = (ArrayList<HashMap<String, Object>>) obj;
                    subHistoryList.addAll(historyList);
                }
                bindUI();
            }
        });

        FireStoreConnectionPool.getInstance().select(this.fireStoreCallbackListener, "history", "id", MainActivity.id, "date");
    }

    private void bindUI()
    {
        // Bind views
        this.resultTextView = this.view.findViewById(R.id.result_textView);
        this.historyListView = this.view.findViewById(R.id.history_listView);

        // Set attributes
        this.adapter = new HistoryListAdapter(getActivity(), R.layout.history_item, this.subHistoryList);
        this.historyListView.setAdapter(this.adapter);
        this.resultTextView.setText("검색 결과 " + this.historyList.size() + "건");

        // Add events
        this.historyListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id)
            {
                historyDetailDialog = new HistoryDetailDialog(getActivity(), R.style.Theme_Dialog, subHistoryList.get(position));
                historyDetailDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                historyDetailDialog.setDialogSetting();

                WindowManager.LayoutParams params = historyDetailDialog.getWindow().getAttributes();

                params.width = 1200;
                params.height = 1360;
                historyDetailDialog.getWindow().setAttributes(params);

                historyDetailDialog.setUpdateComponentListener(new HistoryDetailDialog.UpdateComponentListener() {
                    @Override
                    public void updateComponent(Object obj)
                    {
                        if( (boolean) obj )
                        {
                            Map<String, Object> map = subHistoryList.get(position);
                            subHistoryList.remove(position);
                            historyList.remove(map);
                            adapter.notifyDataSetChanged();
                            resultTextView.setText("검색 결과 " + subHistoryList.size() + "건");
                        }
                    }
                });

                historyDetailDialog.show();
            }
        });
    }

    private void allSearchCategoryViewGone()
    {
        this.searchEditText.setVisibility(EditText.GONE);
        this.splitterTextView.setVisibility(TextView.GONE);
        this.genderSpinner.setVisibility(Spinner.GONE);
        this.genreSpinner.setVisibility(Spinner.GONE);
        this.satisfactionSpinner1.setVisibility(Spinner.GONE);
        this.satisfactionSpinner2.setVisibility(Spinner.GONE);
        this.octaveSpinner1.setVisibility(Spinner.GONE);
        this.octaveSpinner2.setVisibility(Spinner.GONE);
        this.noteSpinner1.setVisibility(Spinner.GONE);
        this.noteSpinner2.setVisibility(Spinner.GONE);
        this.pitchChangeSpinner1.setVisibility(Spinner.GONE);
        this.pitchChangeSpinner2.setVisibility(Spinner.GONE);
        this.dateEditText1.setVisibility(EditText.GONE);
        this.dateEditText2.setVisibility(EditText.GONE);
    }

    private String trimAll(String str)
    {
        return str.replaceAll("\\s", "");
    }

    private double getMaxNoteValue(String str)
    {
        double value = 0;
        int index = 0;

        try
        {
            value = Double.parseDouble(str.substring(index, index + 1)) * 10;
        }
        catch (Exception e)
        {
            index = str.length() - 1;
        }
        finally
        {
            value = Double.parseDouble(str.substring(index, index + 1)) * 10;

            if( index > 0 )
                index = 0;
            else
                index = 1;

            if( str.charAt(index) == 'c' )
                value += 0;
            else if( str.charAt(index) == 'd' )
                value += 1;
            else if( str.charAt(index) == 'e' )
                value += 2;
            else if( str.charAt(index) == 'f' )
                value += 3;
            else if( str.charAt(index) == 'g' )
                value += 4;
            else if( str.charAt(index) == 'a' )
                value += 5;
            else if( str.charAt(index) == 'b' )
                value += 6;

            if( str.contains("#") )
                value += 0.5;

            return value;
        }
    }

    private String toDateString(String str, int defaultValue)
    {
        String value;
        String dates[] = str.split("-");

        int year = defaultValue;
        int month = defaultValue;
        int date = defaultValue;
        int hour = defaultValue;
        int minute = defaultValue;

        try
        {
            if (dates.length > 1)
            {
                year = Integer.parseInt(dates[0]);

                if (dates.length > 2)
                {
                    month = Integer.parseInt(dates[1]);

                    if (dates.length == 3)
                    {
                        date = Integer.parseInt(dates[2].split(" ")[0]);

                        if( dates[2].split(" ").length > 1 )
                        {
                            String times[] = dates[2].split(" ")[1].split(":");

                            if (times.length > 1)
                            {
                                hour = Integer.parseInt(times[0]);

                                if (times.length == 2)
                                    minute = Integer.parseInt(times[1]);
                                else
                                    return null;
                            }
                            else
                                return null;
                        }
                    }
                    else
                        return null;
                }
            }

            value = String.format("%04d-%02d-%02d %02d:%02d", year, month, date, hour, minute);

            return value;
        }
        catch (Exception e)
        {
            return null;
        }
    }
}
