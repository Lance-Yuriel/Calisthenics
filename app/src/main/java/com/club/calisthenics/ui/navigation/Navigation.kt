package com.club.calisthenics.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.club.calisthenics.feature.admin.AdminScreen
import com.club.calisthenics.feature.admin.CreateEventScreen
import com.club.calisthenics.feature.admin.PendingMembersScreen
import com.club.calisthenics.feature.badges.BadgesScreen
import com.club.calisthenics.feature.events.EventDetailScreen
import com.club.calisthenics.feature.events.EventsScreen
import com.club.calisthenics.feature.home.ui.HomeScreen
import com.club.calisthenics.feature.profile.ProfileScreen
import com.club.calisthenics.feature.skills.SkillsScreen

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Home : Screen("home", "Home", Icons.Filled.Home)
    object Events : Screen("events", "Events", Icons.Filled.CalendarMonth)
    object Badges : Screen("badges", "Badges", Icons.Filled.Badge)
    object Skills : Screen("skills", "Skills", Icons.Filled.FitnessCenter)
    object Profile : Screen("profile", "Profile", Icons.Filled.Person)
    object Admin : Screen("admin", "Admin", Icons.Filled.AdminPanelSettings)
    object PendingMembers : Screen("pending_members", "Pending Members", Icons.Filled.Group)
    object CreateEvent : Screen("create_event", "Create Event", Icons.Filled.Add)
    object EventDetail : Screen("event_detail/{eventId}", "Event Detail", Icons.Filled.Event) {
        fun createRoute(eventId: String) = "event_detail/$eventId"
    }
}

val bottomNavItems = listOf(
    Screen.Home,
    Screen.Events,
    Screen.Badges,
    Screen.Skills,
    Screen.Profile
)

@Composable
fun CalisthenicsNavHost(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onEventClick = { eventId ->
                    navController.navigate(Screen.EventDetail.createRoute(eventId))
                }
            )
        }
        composable(Screen.Events.route) {
            EventsScreen(
                onEventClick = { eventId ->
                    navController.navigate(Screen.EventDetail.createRoute(eventId))
                }
            )
        }
        composable(
            route = Screen.EventDetail.route,
            arguments = listOf(navArgument("eventId") { type = NavType.StringType })
        ) {
            EventDetailScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
        composable(Screen.Badges.route) {
            BadgesScreen()
        }
        composable(Screen.Skills.route) {
            SkillsScreen()
        }
        composable(Screen.Profile.route) {
            ProfileScreen(
                onNavigateToAdmin = { navController.navigate(Screen.Admin.route) }
            )
        }
        composable(Screen.Admin.route) {
            AdminScreen(
                onBack = { navController.popBackStack() },
                onNavigateToPendingMembers = { navController.navigate(Screen.PendingMembers.route) },
                onNavigateToCreateEvent = { navController.navigate(Screen.CreateEvent.route) }
            )
        }
        composable(Screen.PendingMembers.route) {
            PendingMembersScreen(
                onBack = { navController.popBackStack() }
            )
        }
        composable(Screen.CreateEvent.route) {
            CreateEventScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}
