# Prize Wheel View #


## About the library

This library is used on my [Chef Buddy](https://github.com/abicelis/ChefBuddy) app. It displays a rotating fling-able prize wheel, with settable sections (the little pizza shaped slices?). 
Whenever the wheel settles after being flung, the library notifies a listening class about the winning section. 

The sections can be programatically set, and they require a custom List of Section objects.
Please see the demo below!

![](https://github.com/abicelis/PrizeWheelView/blob/master/graphics/prize_wheel_view_demo.gif)



## Sample app

TODO UPLOAD APP TO PLAY STORE

<!--<a target="_blank" href='https://play.google.com/store/apps/details?id=ve.com.abicelis.prizewheelsample&pcampaignid=MKT-Other-global-all-co-prtnr-py-PartBadge-Mar2515-1'><img alt='Get it on Google Play' src='https://play.google.com/intl/en_us/badges/images/generic/en_badge_web_generic.png' width="240px"/></a>-->


## Usage


1) **Add the dependency to your gradle file**
```javascript
	TODOO ADD DEPENDENCY
	dependencies {
		compile 've.com.abicelis.prizewheelview:prizewheelview:1.0.0'
	}
```

2) **Add the view to your layout**
```xml
	<ve.com.abicelis.prizewheellib.PrizeWheelView
	android:id="@+id/home_prize_wheel_view"
	android:layout_width="match_parent"
	android:layout_height="match_parent"
	android:layout_gravity="center"
	/>
```

*Note that you can set **layout_width** and **layout_height** to predefined values, or one or both to **match_parent**. The View will take as much space as it can, while still being square.*



3) **In your view class (activity/fragment)**
```java
	//Get the wheel view
	wheelView = (PrizeWheelView) findViewById(R.id.home_prize_wheel_view);
	
	//Populate a List of Sections
	List<WheelSection> wheelSections = new ArrayList<>();
	wheelSections.add(new WheelBitmapSection(someBitmap));
	wheelSections.add(new WheelDrawableSection(R.drawable.some_drawable));
	wheelSections.add(new WheelColorSection(R.color.some_color));
	
	//Set those sections
	wheelView.setWheelSections(wheelSections);
	
	//Finally, generate wheel
	wheelView.generateWheel();
```


4) **Listen when the wheel has settled**
```java
	wheelView.setWheelSettledListener(new ve.com.abicelis.prizewheellib.WheelSettledListener() {
	    @Override
	    public void onWheelSettled(int sectionIndex, double angle) {
	        //Handle wheel settle here
	    }
	});
```


5) **Set even more options**
```java
	wheelView.setMarkerPosition(MarkerPosition.TOP_RIGHT);
	
	wheelView.setWheelBorderLineColor(R.color.border);
	wheelView.setWheelBorderLineThickness(5);
	
	wheelView.setWheelSeparatorLineColor(R.color.separator);
	wheelView.setWheelSeparatorLineThickness(5);
	
	//Set onSettled listener
	wheelView.setWheelSettledListener(new WheelSettledListener());
```
*Note that **wheelView.generateWheel();** must be called **after** setting all the options!!*

For more options and code, please check the [sample project](https://github.com/abicelis/PrizeWheelView/blob/master/PrizeWheelSample/)

## Software used

* [Android Studio 2.3.3 IDE](https://developer.android.com/studio/index.html) - IDE


## Authors

* **Alejandro Bicelis** - *Coding* - [abicelis](https://github.com/abicelis)


## License

This project is licensed under the MIT License - see the [LICENSE](https://github.com/abicelis/PrizeWheelView/blob/master/LICENSE) file for details

