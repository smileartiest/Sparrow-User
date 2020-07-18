package com.smile.atozapp.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.smile.atozapp.activitiespage.ViewDressDetails;
import com.smile.atozapp.activitiespage.ViewMarketDetails;
import com.smile.atozapp.controller.AppUtil;
import com.smile.atozapp.R;
import com.smile.atozapp.parameters.HomePagePicsParameters;
import com.smile.atozapp.models.SliderAdapter;

import java.util.ArrayList;

public class Home extends Fragment {

    View v;
    SliderView sliderView;
    ArrayList<String> imglist = new ArrayList<>();
    TextView viewall_market,viewall_food,viewall_market1,viewall_dress,viewall_electronics;
    ImageView electronic_pic1,electronic_pic2,electronic_pic3,electronic_pic4,electronic_pic5,electronic_pic6,
              daliy_pic1,daliy_pic2,daliy_pic3,daliy_pic4,daliy_pic5,daliy_pic6,
              food_pic1,food_pic2,food_pic3,food_pic4,
              market_pic1,market_pic2,market_pic3,market_pic4,market_pic5,market_pic6,
              dress_pic1,dress_pic2,dress_pic3;

    public Home() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.frag_home , container , false);
        sliderView = v.findViewById(R.id.home_brandSlider);

        viewall_market = v.findViewById(R.id.home_viewall_market);
        viewall_food = v.findViewById(R.id.home_viewall_market_foods);
        viewall_market1 = v.findViewById(R.id.home_viewall_market1);
        viewall_electronics = v.findViewById(R.id.home_viewall_electronics);
        viewall_dress = v.findViewById(R.id.home_viewall_dress);

        electronic_pic1 = v.findViewById(R.id.home_electronic_pic1);
        electronic_pic2 = v.findViewById(R.id.home_electronic_pic2);
        electronic_pic3 = v.findViewById(R.id.home_electronic_pic3);
        electronic_pic4 = v.findViewById(R.id.home_electronic_pic4);
        electronic_pic5 = v.findViewById(R.id.home_electronic_pic5);
        electronic_pic6 = v.findViewById(R.id.home_electronic_pic6);

        daliy_pic1 = v.findViewById(R.id.home_daily_pci1);
        daliy_pic2 = v.findViewById(R.id.home_daily_pci2);
        daliy_pic3 = v.findViewById(R.id.home_daily_pci3);
        daliy_pic4 = v.findViewById(R.id.home_daily_pci4);
        daliy_pic5 = v.findViewById(R.id.home_daily_pci5);
        daliy_pic6 = v.findViewById(R.id.home_daily_pci6);

        food_pic1 = v.findViewById(R.id.home_foods_pic1);
        food_pic2 = v.findViewById(R.id.home_foods_pic2);
        food_pic3 = v.findViewById(R.id.home_foods_pic3);
        food_pic4 = v.findViewById(R.id.home_foods_pic4);

        market_pic1 = v.findViewById(R.id.home_market_pic1);
        market_pic2 = v.findViewById(R.id.home_market_pic2);
        market_pic3 = v.findViewById(R.id.home_market_pic3);
        market_pic4 = v.findViewById(R.id.home_market_pic4);
        market_pic5 = v.findViewById(R.id.home_market_pic5);
        market_pic6 = v.findViewById(R.id.home_market_pic6);

        dress_pic1 = v.findViewById(R.id.home_dress_pic1);
        dress_pic2 = v.findViewById(R.id.home_dress_pic2);
        dress_pic3 = v.findViewById(R.id.home_dress_pic3);

        getimages();
        getPicters();
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onResume() {
        super.onResume();

        viewall_market.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext() , ViewMarketDetails.class).putExtra("tit","Daily Essential's").putExtra("k","all"));
            }
        });

        viewall_market1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext() , ViewMarketDetails.class).putExtra("tit","Super Market Details").putExtra("k","all"));
            }
        });

        viewall_food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext() , ViewMarketDetails.class).putExtra("tit","Food and Snacks Details").putExtra("k","all"));
            }
        });

        viewall_electronics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext() , ViewDressDetails.class).putExtra("f","electronics").putExtra("tit","All Electronic's Details").putExtra("k","all"));
            }
        });

        viewall_dress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext() , ViewDressDetails.class).putExtra("f","dress").putExtra("tit","All Dress Details").putExtra("k","all"));
            }
        });

        daliy_pic1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext() , ViewMarketDetails.class).putExtra("k","Fruits & Vegetables"));
            }
        });

        daliy_pic2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext() , ViewMarketDetails.class).putExtra("k","Foodgrains , Oil & Masala"));
            }
        });
        daliy_pic3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext() , ViewMarketDetails.class).putExtra("k","Cleaning & Household"));
            }
        });
        daliy_pic4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext() , ViewMarketDetails.class).putExtra("k","Baby Care & Personal Care"));
            }
        });
        daliy_pic5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext() , ViewMarketDetails.class).putExtra("k","Snaks & Branded Foods"));
            }
        });
        daliy_pic6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext() , ViewMarketDetails.class).putExtra("k","Bakery , Cakes & Dairy"));
            }
        });

        electronic_pic1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext() , ViewDressDetails.class).putExtra("f","electronics").putExtra("tit","Mobile Back Case").putExtra("k","Mobile Back Case"));
            }
        });

        electronic_pic2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext() , ViewDressDetails.class).putExtra("f","electronics").putExtra("tit","Mobiles").putExtra("k","Mobiles"));
            }
        });

        electronic_pic3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext() , ViewDressDetails.class).putExtra("f","electronics").putExtra("tit","Charger").putExtra("k","Charger"));
            }
        });

        electronic_pic4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext() , ViewDressDetails.class).putExtra("f","electronics").putExtra("tit","Headset").putExtra("k","Headset"));
            }
        });

        electronic_pic5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext() , ViewDressDetails.class).putExtra("f","electronics").putExtra("tit","Speakers").putExtra("k","Speakers"));
            }
        });

        electronic_pic6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext() , ViewDressDetails.class).putExtra("f","electronics").putExtra("tit","Mens & Womens watch").putExtra("k","Mens & Womens watch"));
            }
        });

        food_pic1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext() , ViewMarketDetails.class).putExtra("k","Beverages"));
            }
        });

        food_pic2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext() , ViewMarketDetails.class).putExtra("k","Snaks & Branded Foods"));
            }
        });

        food_pic3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext() , ViewMarketDetails.class).putExtra("k","Eggs , Meat & Fish"));
            }
        });

        food_pic4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext() , ViewMarketDetails.class).putExtra("k","Bakery , Cakes & Dairy"));
            }
        });

        market_pic1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext() , ViewMarketDetails.class).putExtra("k","Beauty & Hygiene"));
            }
        });

        market_pic2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext() , ViewMarketDetails.class).putExtra("k","Cleaning & Household"));
            }
        });

        market_pic3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext() , ViewMarketDetails.class).putExtra("k","Beverages"));
            }
        });

        market_pic4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext() , ViewMarketDetails.class).putExtra("k","Bakery , Cakes & Dairy"));
            }
        });

        market_pic5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext() , ViewMarketDetails.class).putExtra("k","Baby Care & Personal Care"));
            }
        });

        market_pic6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext() , ViewMarketDetails.class).putExtra("k","Baby Care & Personal Care"));
            }
        });

        dress_pic1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext() , ViewDressDetails.class).putExtra("f","dress").putExtra("k","Mens"));
            }
        });

        dress_pic2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext() , ViewDressDetails.class).putExtra("f","dress").putExtra("k","Womens"));
            }
        });

        dress_pic3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext() , ViewDressDetails.class).putExtra("f","dress").putExtra("k","Kids"));
            }
        });

    }

    public void getimages(){
        AppUtil.OFFERURL.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null){
                    for (DataSnapshot data : dataSnapshot.getChildren()){
                        imglist.add(data.child("picurl").getValue().toString());
                    }
                    viewimage();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    void getPicters(){

        AppUtil.HOMEPAGE.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue()!=null){
                    HomePagePicsParameters h = snapshot.getValue(HomePagePicsParameters.class);

                    Glide.with(getContext()).load(h.getM_case()).listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            return false;
                        }
                    }).into(electronic_pic1);

                    Glide.with(getContext()).load(h.getMobile()).listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            return false;
                        }
                    }).into(electronic_pic2);

                    Glide.with(getContext()).load(h.getCharger1()).listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            return false;
                        }
                    }).into(electronic_pic3);

                    Glide.with(getContext()).load(h.getHeadphone()).listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            return false;
                        }
                    }).into(electronic_pic4);

                    Glide.with(getContext()).load(h.getBlutooth_speaker()).listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            return false;
                        }
                    }).into(electronic_pic5);

                    Glide.with(getContext()).load(h.getWatch1()).listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            return false;
                        }
                    }).into(electronic_pic6);

                    Glide.with(getContext()).load(h.getFruits()).listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            return false;
                        }
                    }).into(daliy_pic1);

                    Glide.with(getContext()).load(h.getFoodgrains()).listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            return false;
                        }
                    }).into(daliy_pic2);

                    Glide.with(getContext()).load(h.getCleaning()).listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            return false;
                        }
                    }).into(daliy_pic3);

                    Glide.with(getContext()).load(h.getPersonal_care()).listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            return false;
                        }
                    }).into(daliy_pic4);

                    Glide.with(getContext()).load(h.getSnacks()).listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            return false;
                        }
                    }).into(daliy_pic5);

                    Glide.with(getContext()).load(h.getBakery1()).listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            return false;
                        }
                    }).into(daliy_pic6);

                    Glide.with(getContext()).load(h.getBeverage1()).listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            return false;
                        }
                    }).into(food_pic1);

                    Glide.with(getContext()).load(h.getSnacks2()).listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            return false;
                        }
                    }).into(food_pic2);

                    Glide.with(getContext()).load(h.getEggs2()).listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            return false;
                        }
                    }).into(food_pic3);

                    Glide.with(getContext()).load(h.getBakery1()).listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            return false;
                        }
                    }).into(food_pic4);

                    Glide.with(getContext()).load(h.getBeauty1()).listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            return false;
                        }
                    }).into(market_pic1);

                    Glide.with(getContext()).load(h.getCleaning()).listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            return false;
                        }
                    }).into(market_pic2);

                    Glide.with(getContext()).load(h.getBeverage1()).listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            return false;
                        }
                    }).into(market_pic3);

                    Glide.with(getContext()).load(h.getBakery1()).listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            return false;
                        }
                    }).into(market_pic4);

                    Glide.with(getContext()).load(h.getBaby_care1()).listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            return false;
                        }
                    }).into(market_pic5);

                    Glide.with(getContext()).load(h.getLadies_care()).listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            return false;
                        }
                    }).into(market_pic6);

                    Glide.with(getContext()).load(h.getMens_dress()).listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            return false;
                        }
                    }).into(dress_pic1);

                    Glide.with(getContext()).load(h.getWomens_dress()).listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            return false;
                        }
                    }).into(dress_pic2);

                    Glide.with(getContext()).load(h.getKids_dress()).listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            return false;
                        }
                    }).into(dress_pic3);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void viewimage()
    {
        SliderAdapter s = new SliderAdapter(getActivity() , imglist);

        sliderView.setSliderAdapter(s);

        sliderView.setIndicatorAnimation(IndicatorAnimations.WORM);
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setIndicatorSelectedColor(Color.WHITE);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
        sliderView.setScrollTimeInSec(5); //set scroll delay in seconds :
        sliderView.startAutoCycle();

    }

}
