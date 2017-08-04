package nano.yallam.bakingapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import nano.yallam.bakingapp.model.Step;


public class StepsListAdapter extends RecyclerView.Adapter<StepsListAdapter.StepsListAdapterViewHolder> {

    private Context mContext;
    private OnStepClickListener mClickHandler;
    private ArrayList<Step> steps;

    /**
     * Constructor method
     *
     * @param stepsList The list of steps to display
     */
    public StepsListAdapter(Context context, ArrayList<Step> stepsList, OnStepClickListener listener) {
        mContext = context;
        steps = stepsList;
        mClickHandler = listener;
    }

    class StepsListAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView mStepName;

        StepsListAdapterViewHolder(View view) {
            super(view);
            mStepName = (TextView) view.findViewById(R.id.tv_recipe_name);
            view.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            mClickHandler.onStepSelected(steps, getAdapterPosition());
        }
    }

    @Override
    public StepsListAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.recipes_list_item, parent, false);

        return new StepsListAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StepsListAdapterViewHolder holder, int position) {
        holder.mStepName.setText(steps.get(position).getShortDescription());
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return steps == null ? 0 : steps.size();
    }

    public void setSteps(ArrayList<Step> stepsList) {
        steps = stepsList;
        notifyDataSetChanged();
    }

    public List<Step> getSteps() {
        return steps;
    }

    public interface OnStepClickListener {
        void onStepSelected(ArrayList<Step> steps, int position);
    }
}
