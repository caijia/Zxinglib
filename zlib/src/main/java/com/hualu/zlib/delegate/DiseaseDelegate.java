package com.hualu.zlib.delegate;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.caijia.adapterdelegate.ItemViewDelegate;
import com.hualu.zlib.R;
import java.util.List;

public class DiseaseDelegate extends ItemViewDelegate<Disease, DiseaseDelegate.InnerViewHolder> {

  @Override
  public InnerViewHolder onCreateViewHolder(LayoutInflater inflater, ViewGroup parent,
      int i) {
    View view = inflater.inflate(R.layout.item_disease, parent, false);
    return new InnerViewHolder(view);
  }

  @Override
  public void onBindViewHolder(List<?> list, Disease item, RecyclerView.Adapter adapter,
      InnerViewHolder holder, int i) {
    holder.tvName.setText(orEmpty(item.getDssTypeName()) + "  " + orEmpty(item.getLineDirect()) + "  " + orEmpty(item.getStake()));
    holder.tvCenterStake.setText(orEmpty(item.getDssCount()) + orEmpty(item.getDssUnit()) + "   " + orEmpty(item.getFindDate()));
    holder.setItem(item);
  }

  private String orEmpty(String s) {
    return s == null ? "" : s;
  }

  @Override public boolean isForViewType(@NonNull Object o) {
    return o instanceof Disease;
  }

  static class InnerViewHolder extends RecyclerView.ViewHolder {

    Disease item;
    TextView tvName;
    TextView tvCenterStake;

    View itemView;

    public void setItem(Disease item) {
      this.item = item;
    }

    public InnerViewHolder(@NonNull View itemView) {
      super(itemView);
      tvName = itemView.findViewById(R.id.tvName);
      tvCenterStake = itemView.findViewById(R.id.tvCenterStake);
    }
  }

}
