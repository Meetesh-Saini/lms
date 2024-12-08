import { Routes } from '@angular/router';
import { AuthComponent } from './auth/auth.component';
import { HomeComponent } from './home/home.component';
import { CoursesComponent } from './courses/courses.component';
import { ModulesPageComponent } from './modules-page/modules-page.component';
import { ContentPageComponent } from './content-page/content-page.component';
import { ContentViewerComponent } from './content-viewer/content-viewer.component';

export const routes: Routes = [
    { path: 'home', component: HomeComponent },
    { path: 'courses', component: CoursesComponent },
    { path: "auth", component: AuthComponent },
    { path: 'courses/:courseId/modules', component: ModulesPageComponent },
    { path: 'courses/:courseId/modules/:moduleId/content', component: ContentPageComponent },
    { path: 'courses/:courseId/modules/:moduleId/content/:contentId/view', component: ContentViewerComponent },
    { path: '', redirectTo: '/home', pathMatch: 'full' }
];
