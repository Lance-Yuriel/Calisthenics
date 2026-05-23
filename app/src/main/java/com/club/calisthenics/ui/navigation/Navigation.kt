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
import com.club.calisthenics.feature.admin.AdminScreen
import com.club.calisthenics.feature.admin.EditBadgeScreen
import com.club.calisthenics.feature.admin.EditEventScreen
import com.club.calisthenics.feature.admin.EditSkillScreen
import com.club.calisthenics.feature.admin.ManageBadgesScreen
import com.club.calisthenics.feature.admin.ManageEventsScreen
import com.club.calisthenics.feature.admin.PendingMembersScreen
import com.club.calisthenics.feature.admin.SkillsManagementScreen
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
    object ManageEvents : Screen("manage_events", "Manage Sessions", Icons.Filled.CalendarToday)
    object EditEvent : Screen("edit_event/{eventId}", "Edit Session", Icons.Filled.Edit) {
        fun createRoute(eventId: String?) = if (eventId == null) "edit_event/new" else "edit_event/$eventId"
    }
    object ManageBadges : Screen("manage_badges", "Manage Badges", Icons.Filled.MilitaryTech)
    object EditBadge : Screen("edit_badge/{badgeId}", "Edit Badge", Icons.Filled.Edit) {
        fun createRoute(badgeId: String?) = if (badgeId == null) "edit_badge/new" else "edit_badge/$badgeId"
    }
    object SkillsManagement : Screen("skills_management", "Manage Skills", Icons.Filled.Settings)
    object EditSkill : Screen("edit_skill/{skillId}", "Edit Skill", Icons.Filled.Edit) {
        fun createRoute(skillId: String?) = if (skillId == null) "edit_skill/new" else "edit_skill/$skillId"
    }
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
                onNavigateToCreateEvent = { navController.navigate(Screen.CreateEvent.route) },
                onNavigateToManageSkills = { navController.navigate(Screen.SkillsManagement.route) },
                onNavigateToManageEvents = { navController.navigate(Screen.ManageEvents.route) },
                onNavigateToManageBadges = { navController.navigate(Screen.ManageBadges.route) }
            )
        }
        composable(Screen.PendingMembers.route) {
            PendingMembersScreen(
                onBack = { navController.popBackStack() }
            )
        }
        composable(Screen.CreateEvent.route) {
            EditEventScreen(
                eventId = null,
                onBack = { navController.popBackStack() }
            )
        }
        composable(Screen.ManageEvents.route) {
            ManageEventsScreen(
                onBack = { navController.popBackStack() },
                onNavigateToCreateEvent = { navController.navigate(Screen.EditEvent.createRoute(null)) },
                onNavigateToEditEvent = { id -> navController.navigate(Screen.EditEvent.createRoute(id)) }
            )
        }
        composable(
            route = Screen.EditEvent.route,
            arguments = listOf(navArgument("eventId") { type = NavType.StringType })
        ) { backStackEntry ->
            val eventId = backStackEntry.arguments?.getString("eventId")?.takeIf { it != "new" }
            EditEventScreen(
                eventId = eventId,
                onBack = { navController.popBackStack() }
            )
        }
        composable(Screen.ManageBadges.route) {
            ManageBadgesScreen(
                onBack = { navController.popBackStack() },
                onNavigateToCreateBadge = { navController.navigate(Screen.EditBadge.createRoute(null)) },
                onNavigateToEditBadge = { id -> navController.navigate(Screen.EditBadge.createRoute(id)) }
            )
        }
        composable(
            route = Screen.EditBadge.route,
            arguments = listOf(navArgument("badgeId") { type = NavType.StringType })
        ) { backStackEntry ->
            val badgeId = backStackEntry.arguments?.getString("badgeId")?.takeIf { it != "new" }
            EditBadgeScreen(
                badgeId = badgeId,
                onBack = { navController.popBackStack() }
            )
        }
        composable(Screen.SkillsManagement.route) {
            SkillsManagementScreen(
                onBack = { navController.popBackStack() },
                onNavigateToCreateSkill = { navController.navigate(Screen.EditSkill.createRoute(null)) },
                onNavigateToEditSkill = { id -> navController.navigate(Screen.EditSkill.createRoute(id)) }
            )
        }
        composable(
            route = Screen.EditSkill.route,
            arguments = listOf(navArgument("skillId") { type = NavType.StringType })
        ) { backStackEntry ->
            val skillId = backStackEntry.arguments?.getString("skillId")?.takeIf { it != "new" }
            EditSkillScreen(
                skillId = skillId,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
