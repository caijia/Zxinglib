package com.hualu.zlib.delegate;

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

  public DiseaseAdapter(LoadMoreDelegate.OnLoadMoreDelegateListener l) {
    super(true, l);
    DiseaseDelegate delegate = new DiseaseDelegate();
    delegateManager.addDelegate(delegate);
  }

}
