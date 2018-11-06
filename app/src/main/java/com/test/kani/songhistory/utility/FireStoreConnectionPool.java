package com.test.kani.songhistory.utility;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
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

    public void select(final FireStoreCallbackListener listener, final String... args)
    {
        this.db.collection(args[0]).whereEqualTo(args[1], args[2]).orderBy(args[3], Query.Direction.DESCENDING)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
        {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task)
            {
                if( task.isSuccessful() )
                {
                    if( !task.getResult().isEmpty() )
                    {
                        List<Map<String, Object>> list = new ArrayList<> ();

                        for( DocumentSnapshot document : task.getResult() )
                        {
                            Map<String, Object> map = document.getData();
                            map.put("documentId", document.getId());
                            list.add(map);
                        }
                        listener.doNext(true, list);
                    }
                    else
                        listener.doNext(true, null);
                }
                else
                    listener.doNext(false, null);
            }
        });
    }

//    public void selectEqual(final FireStoreCallbackListener listener, final String... args)
//    {
//        this.db.collection(args[0]).whereEqualTo(args[1], args[2])
//                .whereEqualTo(args[3], args[4]).orderBy(args[5], Query.Direction.DESCENDING)
//                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
//        {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task)
//            {
//                if( task.isSuccessful() )
//                {
//                    if( !task.getResult().isEmpty() )
//                    {
//                        List<Map<String, Object>> list = new ArrayList<> ();
//
//                        for( DocumentSnapshot document : task.getResult() )
//                        {
//                            Map<String, Object> map = document.getData();
//                            map.put("documentId", document.getId());
//                            list.add(map);
//                        }
//                        listener.doNext(true, list);
//                    }
//                    else
//                        listener.doNext(true, null);
//                }
//                else
//                    listener.doNext(false, null);
//            }
//        });
//    }

//    public void selectGreaterEqual(final FireStoreCallbackListener listener, final String... args)
//    {
//        this.db.collection(args[0]).whereEqualTo(args[1], args[2])
//                .whereGreaterThanOrEqualTo(args[3], args[4]).orderBy(args[5], Query.Direction.DESCENDING)
//                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
//        {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task)
//            {
//                if( task.isSuccessful() )
//                {
//                    if( !task.getResult().isEmpty() )
//                    {
//                        List<Map<String, Object>> list = new ArrayList<> ();
//
//                        for( DocumentSnapshot document : task.getResult() )
//                        {
//                            Map<String, Object> map = document.getData();
//                            map.put("documentId", document.getId());
//                            list.add(map);
//                        }
//                        listener.doNext(true, list);
//                    }
//                    else
//                        listener.doNext(true, null);
//                }
//                else
//                    listener.doNext(false, null);
//            }
//        });
//    }

//    public void selectGreaterLessEqual(final FireStoreCallbackListener listener, final String... args)
//    {
//        this.db.collection(args[0]).whereEqualTo(args[1], args[2])
//                .whereGreaterThanOrEqualTo(args[3], args[4])
//                .whereLessThanOrEqualTo(args[5], args[6])
//                .orderBy(args[7], Query.Direction.DESCENDING)
//                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
//        {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task)
//            {
//                if( task.isSuccessful() )
//                {
//                    if( !task.getResult().isEmpty() )
//                    {
//                        List<Map<String, Object>> list = new ArrayList<> ();
//
//                        for( DocumentSnapshot document : task.getResult() )
//                        {
//                            Map<String, Object> map = document.getData();
//                            map.put("documentId", document.getId());
//                            list.add(map);
//                        }
//                        listener.doNext(true, list);
//                    }
//                    else
//                        listener.doNext(true, null);
//                }
//                else
//                    listener.doNext(false, null);
//            }
//        });
//    }

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

    public void delete(final FireStoreCallbackListener listener, final String... args)
    {
        this.db.collection(args[0]).document(args[1]).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid)
            {
                listener.doNext(true, true);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                listener.doNext(false, null);
            }
        });
    }
}
