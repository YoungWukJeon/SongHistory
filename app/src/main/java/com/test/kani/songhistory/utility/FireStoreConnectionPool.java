package com.test.kani.songhistory.utility;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class FireStoreConnectionPool
{
    private static final FireStoreConnectionPool ourInstance = new FireStoreConnectionPool();
    private FirebaseFirestore db;

    public static FireStoreConnectionPool getInstance()
    {
        return ourInstance;
    }

    private FireStoreConnectionPool()
    {
        this.db = FirebaseFirestore.getInstance();
    }

    public void selectOne(final FireStoreCallbackListener listener, String... args)
    {
//        this.db.collection(args[0]).whereEqualTo(args[1], args[2]).get()
//        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
//        {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task)
//            {
//                if( task.isSuccessful() )
//                {
//                    if( !task.getResult().isEmpty() )
//                        listener.doNext(true, task.getResult().getDocuments().get(0).getData());
//                    else
//                        listener.doNext(true, null);
//                }
//                else
//                    listener.doNext(false, null);
//            }
//        });

        this.db.collection(args[0]).document(args[1]).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>()
                {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task)
                    {
                        if( task.isSuccessful() )
                        {
                            if( task.getResult().exists() )
                                listener.doNext(true, task.getResult().getData());
                            else
                                listener.doNext(true, null);
                        }
                        else
                            listener.doNext(false, null);
                    }
                });
    }

    public void insert(final FireStoreCallbackListener listener, final Map<String, Object> map, final String... args)
    {
        this.db.collection(args[0]).document(args[1]).set(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid)
                    {
                        listener.doNext(true, true);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                listener.doNext(false,null);
            }
        });
    }

    public void insertNoID(final FireStoreCallbackListener listener, final Map<String, Object> map, final String... args)
    {
        this.db.collection(args[0]).add(map)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference)
                    {
                        listener.doNext(true, documentReference.getId());
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                listener.doNext(false,null);
            }
        });
    }
}
