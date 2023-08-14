package com.rickandmorty.foodbook.view

//import androidx.navigation.Navigation
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.rickandmorty.foodbook.adapter.FoodRecyclerAdapter
import com.rickandmorty.foodbook.databinding.FragmentFoodListBinding
import com.rickandmorty.foodbook.viewmodel.FoodListViewModel



class FoodListFragment : Fragment() {
    //private var foodId = 0
    private lateinit var viewModel : FoodListViewModel
    private val recyclerFoodAdapter = FoodRecyclerAdapter(arrayListOf())
    private lateinit var _binding : FragmentFoodListBinding
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        _binding = FragmentFoodListBinding.inflate(inflater,container,false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(FoodListViewModel::class.java)
        viewModel.refreshData()

        //var myString = "Atıl"
        //myString.MyExt("sAMANCI")

        binding.foodListRecycler.layoutManager = LinearLayoutManager(context)
        binding.foodListRecycler.adapter = recyclerFoodAdapter

        binding.swipeRefreshLayout.setOnRefreshListener {
            binding.swipeRefreshLayout.isRefreshing = false
            binding.foodUploading.visibility = View.VISIBLE
            binding.foodMistakeMessage.visibility = View.GONE
            binding.foodListRecycler.visibility = View.GONE
            viewModel.refreshFromInternet()

        }
        observeLiveData()

    }

    //Gözlemliyeceğimiz durumlara göre neler yapılması gerektiğini belirtmemiz gerekir.
    fun observeLiveData(){

            viewModel.food.observe(viewLifecycleOwner, Observer { food ->


                food?.let {

                    binding.foodListRecycler.visibility = View.VISIBLE
                    recyclerFoodAdapter.foodListUpdate(food)

                }

            })

        viewModel.foodErrorMessage.observe(viewLifecycleOwner, Observer{ error ->

            error?.let {

                if (it){

                    binding.foodMistakeMessage.visibility = View.VISIBLE
                    binding.foodListRecycler.visibility = View.GONE
                }else{

                    binding.foodMistakeMessage.visibility = View.GONE
                }

            }

        })


        viewModel.foodUploading.observe(viewLifecycleOwner, Observer {  loading ->

            loading?.let {

                if(it){
                    binding.foodMistakeMessage.visibility = View.GONE
                    binding.foodListRecycler.visibility = View.GONE
                    binding.foodUploading.visibility = View.VISIBLE
                }else{

                    binding.foodUploading.visibility = View.GONE
                }

            }

        })

    }




    /*override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*arguments?.let {

            foodId = FoodListFragmentArgs.fromBundle(it).foodId
            println(foodId)
        }*/


        /*/binding.foodDetailButton.setOnClickListener {

            val action = FoodListFragmentDirections.actionFoodListFragmentToFoodDetailFragment(3)
            Navigation.findNavController(it).navigate(action)
        }*/

    }*/

}