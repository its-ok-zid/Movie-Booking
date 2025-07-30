import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-book-ticket-page',
  templateUrl: './book-ticket-page.component.html',
  styleUrls: ['./book-ticket-page.component.scss']
})
export class BookTicketPageComponent implements OnInit {
  movieName = '';
  theatreName = '';
  totalTickets = 0;

  constructor(private route: ActivatedRoute, private router: Router) {}

  ngOnInit(): void {
    const role = localStorage.getItem('role');
    if (role !== 'USER') {
      // If not USER, redirect to login or admin
      if (role === 'ADMIN') {
        this.router.navigate(['/admin']);
      } else {
        this.router.navigate(['/login']);
      }
      return;
    }
    this.movieName = this.route.snapshot.paramMap.get('movieName') || '';
    this.theatreName = this.route.snapshot.paramMap.get('theatreName') || '';
    // Optionally, get totalTickets from route or service
    const ticketsParam = this.route.snapshot.queryParamMap.get('totalTickets');
    this.totalTickets = ticketsParam ? +ticketsParam : 0;
  }

  onBookingSuccess(): void {
    this.router.navigate(['/movies']); // Go back to movies list after booking
  }

  closeBooking(): void {
    this.router.navigate(['/movies']); // Go back to movies list if closed
  }
}
