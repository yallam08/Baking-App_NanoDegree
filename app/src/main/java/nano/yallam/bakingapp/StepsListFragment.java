package nano.yallam.bakingapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import nano.yallam.bakingapp.model.Step;


public class StepsListFragment extends Fragment {

    StepsListAdapter.OnStepClickListener mClickListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mClickListener = (StepsListAdapter.OnStepClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnStepClickListener");
        }
    }


    // Mandatory empty constructor
    public StepsListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_steps_list, container, false);

        RecyclerView rv = (RecyclerView) rootView.findViewById(R.id.steps_rv);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv.setHasFixedSize(true);

        if (getActivity().getIntent().hasExtra(MainActivity.RECIPE_STEPS_LIST_KEY)) {
            final ArrayList<Step> steps = (ArrayList<Step>) getActivity().getIntent().getSerializableExtra(MainActivity.RECIPE_STEPS_LIST_KEY);
            StepsListAdapter mAdapter = new StepsListAdapter(getContext(), steps, mClickListener);

            rv.setAdapter(mAdapter);
        }

        // Return the root view
        return rootView;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mClickListener = null;
    }
}
