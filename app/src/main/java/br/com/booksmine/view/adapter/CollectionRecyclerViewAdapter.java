package br.com.booksmine.view.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import br.com.booksmine.R;
import br.com.booksmine.databinding.GridItemCollectionBinding;
import br.com.booksmine.model.pojo.Book;
import br.com.booksmine.model.realm.po.RealmBook;
import br.com.booksmine.model.realm.util.RealmUtil;
import br.com.booksmine.view.listener.ClickListener;
import br.com.booksmine.view.listener.LongClickListener;
import io.realm.RealmBasedRecyclerViewAdapter;
import io.realm.RealmResults;
import io.realm.RealmViewHolder;

/**
 * Developed by.:   @thiagozg on 23/02/2017.
 * E-mail.:         thiagozg1995@gmail.com
 * GitHub.:         https://github.com/thiagozg/
 * Google Play.:    https://play.google.com/store/apps/developer?id=Thiago+Giacomini
 */

public class CollectionRecyclerViewAdapter extends
        RealmBasedRecyclerViewAdapter<RealmBook, CollectionRecyclerViewAdapter.ViewHolder> {

    private RealmResults<RealmBook> realmResults;
    private LongClickListener longClickListener;
    private ClickListener clickListener;

    public CollectionRecyclerViewAdapter(Context context, RealmResults<RealmBook> realmResults,
                                         boolean automaticUpdate, boolean animateIdType,
                                         LongClickListener longClickListener,
                                         ClickListener clickListener) {
        super(context, realmResults, automaticUpdate, animateIdType);
        this.realmResults = realmResults;
        this.longClickListener = longClickListener;
        this.clickListener = clickListener;
    }

    @Override
    public ViewHolder onCreateRealmViewHolder(ViewGroup parent, int viewType) {
        GridItemCollectionBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.grid_item_collection,
                parent,
                false
        );

        ViewHolder holder = new ViewHolder(binding);

        holder.itemView.setOnLongClickListener(view -> {

            if (longClickListener != null) {
                int position = holder.getAdapterPosition();
                longClickListener.onBookLongClick(getRealmBook(position));
            }

            return true;
        });

        holder.itemView.setOnClickListener(view -> {
            if (clickListener != null) {
                int position = holder.getAdapterPosition();
                Book book = RealmUtil.convertPOBookToParcelableBook(getRealmBook(position));
                clickListener.onBookClick(book, false);
            }
        });

        return holder;
    }

    private RealmBook getRealmBook(int position) {
        return this.realmResults.get(position);
    }

    @Override
    public void onBindRealmViewHolder(ViewHolder holder, int position) {
        RealmBook realmBook = realmResults.get(position);

        if ("".equals(realmBook.getPublishedDate())
                || realmBook.getPublishedDate() == null)
            holder.removeView(2);

        holder.binding.setBook(realmBook);
        holder.binding.executePendingBindings();
    }

    public class ViewHolder extends RealmViewHolder {

        GridItemCollectionBinding binding;

        public ViewHolder(GridItemCollectionBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void removeView(int position) {
            binding.rlGrid.removeView(binding.rlGrid.getChildAt(position));
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            layoutParams.addRule(RelativeLayout.BELOW, this.binding.tvTitle.getId());
            layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
            this.binding.tvAuthors.setLayoutParams(layoutParams);
        }

    }

}
