package com.keane9301.myapp001.ViewsAndAdapters;

import com.keane9301.myapp001.Database.Parts.PartEntity;

public interface OnPartClickListener {
    void OnPartClick (int adapterPos, PartEntity part);
}
