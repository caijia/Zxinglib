package com.hualu.zlib.delegate;

import androidx.annotation.Nullable;
import com.caijia.adapterdelegate.LoadMoreDelegationAdapter;
import com.caijia.adapterdelegate.delegate.LoadMoreDelegate;

/**
 * <p>
 * DiseaseAdapter
 * </p>
 *
 * @author cai.jia
 * @since 2022-07-11 11:05
 */
public class DiseaseAdapter extends LoadMoreDelegationAdapter {

  public DiseaseAdapter(@Nullable LoadMoreDelegate.OnLoadMoreDelegateListener l) {
    super(true, l);
    DiseaseDelegate delegate = new DiseaseDelegate();
    delegateManager.addDelegate(delegate);
  }

}
