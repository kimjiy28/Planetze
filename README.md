# CSCB07 Group Project - Planetze
Planetze is a sustainability platform designed to empower individuals and employees to track, reduce, and offset their carbon footprint. With the rising importance of climate action, Planetze provides users with personalized insights into their environmental impact and offers real-time data on their daily carbon emissions. Users can adopt eco-friendly habits, and contribute to certified carbon offset projects, making climate action accessible and achievable for everyone

# Overall Structure
Download & Open App: The user downloads the app and opens it for the first time. First-Time User Onboarding: The app welcomes users and prompts them to begin the initial setup.

Introduction to Carbon Footprint Calculation: The app briefly explains that it will ask a few questions to calculate their annual carbon footprint. This step will only happen once unless the user decides to recalculate later.

Annual Carbon Footprint: The user answers a few questions about their lifestyle to calculate their yearly carbon footprint and compare it to the national average. (First Time Only)

Navigation to Main Menu: The user is then directed to the Eco Tracker to monitor daily emissions.

Access to Other Menus: After Eco Tracker, users can explore Eco Gauge, and Eco Hub.

# Key Components
Eco Tracker: Tracks users’ carbon emissions based on their daily activities.

Eco Gauge: A visual representation of progress toward carbon reduction goals, motivating users with clear, tangible results.

Eco Hub: A resource center with educational content, and sustainability tips to help users stay informed and engaged.

# Code Use
1. Clone repo: https://github.com/kimjiy28/Planetze.git
3. Open in Android Studio
4. Select Emulator
5. Click Run

# Dependencies
    // App UI
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.activity)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    testImplementation (libs.junit.junit.v412)
    testImplementation(libs.mockito.mockito.all)
    testImplementation (libs.mockito.mockito.all)


    // BOM for firebase platform
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)

    // Firebase Products
    implementation(libs.google.firebase.database)
    implementation(libs.firebase.auth)

    // External Resources
    implementation("com.github.cjhandroid:WaveProgressBar:v1.0.0")
    implementation("com.github.anastr:speedviewlib:1.6.1")

# Contributors
@bryntam, @kimjiy28, @lllJYlll, @Priscilla, @rize7425, @wenkanhh

# Assumptions Made
Annual Carbon Footprint:
  1. Housing carbon emissions may be negative 
  2. Total carbon emissions may be negative  
  3. Q21: How often do you recycle? For quarterly buyers, emissions reduction is calculated as follows
       Occasional Recycling : Reduction: 18 kg CO2e/year
       Frequent Recycling : Reduction: 36 kg CO2e/year
       Always Recycling :Reduction: 60 kg CO2e/year


