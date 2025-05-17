
# UTS Pemrograman Berorientasi Obyek 2
<ul>
  <li>Mata Kuliah: Pemrograman Berorientasi Obyek 2</li>
  <li>Dosen Pengampu: <a href="https://github.com/Muhammad-Ikhwan-Fathulloh">Muhammad Ikhwan Fathulloh</a></li>
</ul>

## Profil
<ul>
  <li>Nama: Nur Anisa</li>
  <li>NIM: 23552011171</li>
  <li>Studi Kasus: Aplikasi To-Do List dengan Login dan Filter Status Tugas</li>
</ul>

## Judul Studi Kasus
<p>Aplikasi To-Do List Berbasis Web dengan Fitur Otentikasi Pengguna dan Filter Status Tugas</p>

## Penjelasan Studi Kasus
<p>Aplikasi ini memungkinkan pengguna untuk mencatat, mengedit, dan menghapus tugas harian mereka setelah login.
Tugas dapat difilter berdasarkan status (selesai atau belum selesai). Aplikasi dibangun menggunakan Spring Boot dan
mengimplementasikan prinsip-prinsip Pemrograman Berorientasi Objek.</p>

## Penjelasan 4 Pilar OOP dalam Studi Kasus

### 1. Inheritance
<p>Kelas <code>CustomUserDetailsService</code> mewarisi dari <code>UserDetailsService</code> milik Spring Security untuk mengatur autentikasi pengguna.</p>

### 2. Encapsulation
<p>Kelas model seperti <code>ToDo</code> dan <code>User</code> menyembunyikan field melalui penggunaan modifier <code>private</code> dan menyediakan akses melalui getter dan setter.</p>

### 3. Polymorphism
<p>Spring Boot memanfaatkan polymorphism pada interface seperti <code>JpaRepository</code> untuk memungkinkan berbagai implementasi metode tanpa mengubah pemanggilnya.</p>

### 4. Abstract
<p>Interface seperti <code>ToDoRepository</code> dan <code>UserRepository</code> adalah contoh abstraction yang menyembunyikan detail implementasi database dari logika aplikasi.</p>

## Demo Proyek
<ul>
  <li>Github: <a href="">Github</a></li>
  <li>Youtube: <a href="">(https://youtu.be/UTF6IisSD5o)</a></li>
</ul>
