import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { MatListModule } from '@angular/material/list';
import { MatButtonModule } from '@angular/material/button';
import { ApiService } from '../api.service';
import { NotificationService } from '../notification.service';
import { CommonModule, NgIf } from '@angular/common';
import { NavbarComponent } from '../navbar/navbar.component';
import { MatDialog } from '@angular/material/dialog';
import { ContentDialogComponent } from '../content-dialog/content-dialog.component';
import { MatCard, MatCardActions, MatCardHeader, MatCardModule, MatCardTitle } from '@angular/material/card';

@Component({
  selector: 'app-content-page',
  standalone: true,
  imports: [MatListModule, MatButtonModule, CommonModule, NavbarComponent, MatCard, MatCardHeader, MatCardTitle, MatCardActions, MatCardModule, NgIf],
  templateUrl: './content-page.component.html',
  styleUrls: ['./content-page.component.scss'],
})
export class ContentPageComponent implements OnInit {
  moduleId: string | null = null;
  courseId: string | null = null;
  contents: any[] = [];
  username: string | null = null;
  instructor: string | null = null;

  constructor(private route: ActivatedRoute, private router: Router, private apiService: ApiService, private notification: NotificationService, private dialog: MatDialog) { }

  fetchContent() {
    this.apiService.get(`/contents/module/${this.moduleId}`).subscribe({
      next: (data: any) => {
        this.contents = data
        if (this.contents.length !== 0) {
          this.apiService.get(`/modules/${this.moduleId}`).subscribe({
            next: (response: any) => {
              this.instructor = response.data.course.instructor.username;
            },
            error: () => { },
          });
        } else {
          this.instructor = null;
        }
      },
      error: (err) => this.notification.show('Failed to fetch content:', true),
    });
  }

  ngOnInit(): void {
    this.username = this.apiService.getUsername();
    this.moduleId = this.route.snapshot.paramMap.get('moduleId');
    this.courseId = this.route.snapshot.paramMap.get('courseId');
    console.log('Module ID:', this.moduleId);

    // Fetch content using API
    if (this.moduleId) {
      this.fetchContent();
    }
  }

  goBack(): void {
    this.router.navigate([`/courses/${this.courseId}/modules`]);
  }

  addContent(): void {
    let moduleId = this.moduleId;
    const dialogRef = this.dialog.open(ContentDialogComponent, {
      data: { moduleId }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.fetchContent();
      }
    });
  }


  deleteContent(contentId: number): void {
    if (confirm('Are you sure you want to delete this content?')) {
      this.apiService.delete(`/contents/${contentId}`).subscribe({
        next: () => {
          this.fetchContent();
          this.notification.show('Content deleted successfully');
        },
        error: () => this.notification.show('Failed to delete content', true),
      });
    }
  }

  openContent(contentId: number): void {
    this.router.navigate([`/courses/${this.courseId}/modules/${this.moduleId}/content/${contentId}/view`]);
  }


}
