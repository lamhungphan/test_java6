<script setup>
import {ref, onMounted} from 'vue'
import {useUserStore} from '@/stores/userStore'
import {storeToRefs} from 'pinia'

const store = useUserStore()
const { users, loading, total, currentPage, pageSize } = storeToRefs(store)

const form = ref({
  id: '',
  password: '',
  fullName: '',
  email: '',
  role: ''
})

const keyword = ref('')
const isEditing = ref(false)

const submitForm = async () => {
  if (isEditing.value) {
    await store.updateUser(form.value.id, form.value)
  } else {
    await store.createUser(form.value)
  }
  await store.fetchUsers({})
  resetForm()
}

const editUser = (user) => {
  form.value = {...user}
  isEditing.value = true
}

const deleteUser = async (id) => {
  await store.deleteUser(id)
  await store.fetchUsers({})
}

const resetForm = () => {
  form.value = {
    id: '',
    password: '',
    fullName: '',
    email: '',
    role: ''
  }
  isEditing.value = false
}

const search = async () => {
  await store.fetchUsers({keyword: keyword.value})
}

const changePage = async (page) => {
  await store.fetchUsers({ keyword: keyword.value, page, size: pageSize.value })
}

onMounted(() => {
  store.fetchUsers({})
})
</script>

<template>
  <div class="user-manager container-fluid d-flex flex-column align-items-center py-5">

    <div class="form-box p-4 rounded">

      <div class="input-group mb-3">
        <input
            v-model="keyword"
            type="text"
            class="form-control"
            placeholder="Type your name or email here..."
        />
        <button class="btn btn-primary" @click="search">Search</button>
      </div>

      <h3 class="text-white fw-bold text-center mb-2">User Management - CRUD</h3>
      <form @submit.prevent="submitForm">
        <input v-model="form.id" class="form-control mb-2" placeholder="id1" required :readonly="isEditing"/>
        <input v-model="form.password" type="password" class="form-control mb-2" placeholder="**********" required/>
        <input v-model="form.fullName" class="form-control mb-2" placeholder="What's your name" required/>
        <input v-model="form.email" type="email" class="form-control mb-2" placeholder="your-name@fpt.edu.vn" required/>

        <div class="mb-3 text-white">
          Role:
          <select v-model="form.role" class="form-select mb-3">
            <option value="ADMIN">Admin</option>
            <option value="USER">User</option>
            <option value="CUSTOMER">Customer</option>
          </select>

        </div>

        <div class="d-flex gap-2 justify-content-center">
          <button type="submit" class="btn btn-primary">Create</button>
          <button v-if="isEditing" @click="submitForm" class="btn btn-warning" type="button">Update</button>
          <button v-if="isEditing" @click="deleteUser(form.id)" class="btn btn-danger" type="button">Delete</button>
          <button type="button" @click="resetForm" class="btn btn-success">Reset</button>
        </div>
      </form>
    </div>

    <!-- Table -->
    <div class="table-responsive mt-5 w-100 px-3">
      <table class="table table-bordered text-white text-center table-dark table-striped align-middle">
        <thead class="table-light text-dark">
        <tr>
          <th>No</th>
          <th>User ID</th>
          <th>Password</th>
          <th>Full-name</th>
          <th>Email</th>
          <th>Role</th>
          <th>Edit</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="(user, index) in users" :key="user.id">
          <td>{{ index + 1 }}</td>
          <td>{{ user.id }}</td>
          <td>{{ user.password }}</td>
          <td>{{ user.fullName }}</td>
          <td>{{ user.email }}</td>
          <td>{{ user.role }}</td>
          <td>
            <a href="#" class="text-info me-2" @click.prevent="editUser(user)">Edit</a>
            <a href="#" class="text-danger" @click.prevent="deleteUser(user.id)">Remove</a>
          </td>
        </tr>
        </tbody>
      </table>
    </div>

    <!-- Pagination -->
    <div class="pagination d-flex justify-content-center mt-3">
      <button
          class="btn btn-outline-secondary"
          :disabled="currentPage === 0"
          @click="changePage(currentPage - 1)"
      >
        Previous
      </button>
      <span class="mx-3">Page {{ currentPage + 1 }} of {{ Math.ceil(total / pageSize) }}</span>
      <button
          class="btn btn-outline-secondary"
          :disabled="currentPage === Math.ceil(total / pageSize) - 1"
          @click="changePage(currentPage + 1)"
      >
        Next
      </button>
    </div>

  </div>
</template>

<style scoped>
body {
  background-color: #f2f2f2;
}

.user-manager {
  background-color: #ffffff;
  min-height: 100vh;
}

.form-box {
  width: 100%;
  max-width: 500px;
  background-color: #ffffff;
  border: 1px solid #dcdcdc;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  border-radius: 12px;
}

.table-light th, .table-light td {
  vertical-align: middle;
  background-color: #ffffff;
}

.table-responsive {
  background-color: #f8f9fa;
  border-radius: 12px;
  padding: 1rem;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.05);
}

h3 {
  color: #4f87bb;
}

.text-white-50 {
  color: #6c757d !important;
}

.btn-primary {
  background-color: #007bff;
  border: none;
}

.btn-warning {
  background-color: #ffc107;
  border: none;
}

.btn-danger {
  background-color: #dc3545;
  border: none;
}

.btn-success {
  background-color: #28a745;
  border: none;
}

.badge.bg-dark {
  background-color: #5c6c80;
}

.badge.bg-secondary {
  background-color: #6c757d;
}

a.text-info {
  color: #0d6efd;
}

a.text-danger {
  color: #dc3545;
}
</style>

