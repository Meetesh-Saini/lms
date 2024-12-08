import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute, RouterModule } from '@angular/router';
import { MatListModule } from '@angular/material/list';
import { MatButtonModule } from '@angular/material/button';
import { ApiService } from '../api.service';
import { NotificationService } from '../notification.service';
import { CommonModule, NgIf } from '@angular/common';
import { NavbarComponent } from '../navbar/navbar.component';
import { MatCardModule } from '@angular/material/card';
import { MatDialog } from '@angular/material/dialog';
import { ModuleDialogComponent } from '../module-dialog/module-dialog.component';

@Component({
  selector: 'app-modules-page',
  standalone: true,
  imports: [MatCardModule, RouterModule, MatListModule, MatButtonModule, CommonModule, NavbarComponent, ModuleDialogComponent, NgIf],
  templateUrl: './modules-page.component.html',
  styleUrl: './modules-page.component.scss',
})
export class ModulesPageComponent implements OnInit {
  courseId: string | null = null;
  modules: any[] = [];
  username: string | null = null;

  constructor(private router: Router, private route: ActivatedRoute, private apiService: ApiService, private notification: NotificationService, private dialog: MatDialog) { }

  fetchModules() {
    this.apiService.get(`/modules/course/${this.courseId}`).subscribe({
      next: (data: any) => {
        if (data.success) {
          this.modules = data.data
        } else {
          this.notification.show(data.message, true)
        }
      },
      error: (err) => this.notification.show('Failed to fetch modules', true),
    });
  }

  ngOnInit(): void {
    this.username = this.apiService.getUsername();
    this.courseId = this.route.snapshot.paramMap.get('courseId');
    console.log('Course ID:', this.courseId);
    if (this.courseId) {
      this.fetchModules();
    }
  }

  openModuleContent(moduleId: number): void {
    this.router.navigate([`/courses/${this.courseId}/modules/${moduleId}/content`]);
  }

  deleteModule(moduleId: number): void {
    if (confirm('Are you sure you want to delete this module?')) {
      this.apiService.delete(`/modules/${moduleId}`).subscribe({
        next: () => {
          this.fetchModules();
          this.notification.show('Module deleted successfully');
        },
        error: () => this.notification.show('Failed to delete module', true),
      });
    }
  }

  addModule(): void {
    this.openModuleDialog();
  }

  editModule(module: any): void {
    this.openModuleDialog(module);
  }

  openModuleDialog(module?: any): void {
    const dialogRef = this.dialog.open(ModuleDialogComponent, {
      width: '400px',
      data: { module, courseId: this.courseId },
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.notification.show('Module saved successfully');
        // Refresh the modules list
        this.fetchModules();
      }
    });
  }
}
