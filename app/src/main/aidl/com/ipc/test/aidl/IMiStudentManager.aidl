package com.ipc.test.aidl;

import com.ipc.test.aidl.MiStudent;
import com.ipc.test.aidl.IStudentListSizeChangeListener;
interface IMiStudentManager
{
    List<MiStudent> getMiStudent();
    void addStudent(in MiStudent student);
    boolean remove(in MiStudent student);
    void registerListener(IStudentListSizeChangeListener listener);
    void unregisterListener(IStudentListSizeChangeListener listener);
}
