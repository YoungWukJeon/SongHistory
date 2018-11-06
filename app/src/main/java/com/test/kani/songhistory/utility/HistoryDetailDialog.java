package com.test.kani.songhistory.utility;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.test.kani.songhistory.R;

import java.util.Map;

public class HistoryDetailDialog extends Dialog
{
    private UpdateComponentListener updateComponentListener;
    private Context context;
    private Map<String, Object> map;

    TextView noTextView, titleTextView, singerTextView, genderTextView, genreTextView, maxNoteTextView,
            pitchChangeTextView, dateTextView, satisfactionTextView, etcTextView;

    Button deleteBtn, closeBtn;

    private FireStoreCallbackListener fireStoreCallbackListener;
    private LoadingDialog loadingDialog;

    public void setFireStoreCallbackListener(FireStoreCallbackListener listener)
    {
        this.fireStoreCallbackListener = listener;
    }

    public HistoryDetailDialog(Context context, int themeResId, Map<String, Object> map)
    {
        super(context, themeResId);
        this.setCancelable(false);
        this.context = context;
        this.map = map;
    }

    public void setDialogSetting()
    {
        this.setContentView(R.layout.history_detail_dialog);
        this.init();
        this.bindUI();
    }

    private void init()
    {
        this.setFireStoreCallbackListener(new FireStoreCallbackListener()
        {
            final int TASK_FAILURE = 1;

            @Override
            public void occurError(int errorCode)
            {
                switch (errorCode)
                {
                    case TASK_FAILURE:
                        Log.d("HistoryDetailDialog", "Task is not successful");
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

                if (obj != null)
                {
                    Toast.makeText(context, "삭제되었습니다.", Toast.LENGTH_SHORT).show();
                    updateComponentListener.updateComponent(true);   // 삭제로 닫은 경우
                    dismiss();
                }
            }
        });
    }

    private void bindUI()
    {
        // Bind views
        this.noTextView = findViewById(R.id.no_textView);
        this.titleTextView = findViewById(R.id.title_textView);
        this.singerTextView = findViewById(R.id.singer_textView);
        this.genderTextView = findViewById(R.id.gender_textView);
        this.genreTextView = findViewById(R.id.genre_textView);
        this.maxNoteTextView = findViewById(R.id.maxNote_textView);
        this.pitchChangeTextView = findViewById(R.id.pitchChange_textView);
        this.dateTextView = findViewById(R.id.date_textView);
        this.satisfactionTextView = findViewById(R.id.satisfaction_textView);
        this.etcTextView = findViewById(R.id.etc_textView);
        this.deleteBtn = findViewById(R.id.delete_btn);
        this.closeBtn = findViewById(R.id.close_btn);

        // Set attributes
        this.noTextView.setText(this.map.get("no").toString().trim());
        this.titleTextView.setText(this.map.get("title").toString().trim());
        this.singerTextView.setText(this.map.get("singer").toString().trim());
        this.genderTextView.setText(this.map.get("gender").toString().trim());
        this.genreTextView.setText(this.map.get("genre").toString().trim());
        this.maxNoteTextView.setText(this.map.get("maxNote").toString().trim());
        this.pitchChangeTextView.setText(this.map.get("pitchChange").toString().trim());
        this.dateTextView.setText(this.map.get("date").toString().trim());
        this.satisfactionTextView.setText(this.map.get("satisfaction").toString().trim());
        this.etcTextView.setText(this.map.get("etc").toString().trim());

        // Add events
        this.deleteBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("삭제 하시겠습니까?")
                        .setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id)
                            {
                                dialog.dismiss();

                                if( loadingDialog == null )
                                    loadingDialog = new LoadingDialog(getContext());

                                loadingDialog.show("Deleting history...");

                                FireStoreConnectionPool.getInstance().delete(fireStoreCallbackListener, "history",
                                        (String) map.get("documentId"));
                            }
                        })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id)
                            {
                                dialog.dismiss();
                            }
                        });

                builder.create().show();
            }
        });

        this.closeBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                updateComponentListener.updateComponent(false);  // 그냥 닫은 경우
                dismiss();
            }
        });
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        updateComponentListener.updateComponent(false);     // 그냥 닫은 경우
        dismiss();
    }

    public void setUpdateComponentListener(UpdateComponentListener listener)
    {
        this.updateComponentListener = listener;
    }

    public interface UpdateComponentListener
    {
        void updateComponent(Object obj);
    }
}
