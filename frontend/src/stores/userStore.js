import { defineStore } from 'pinia'
import axios from '@/axios/axios.js' // import từ instance đã config (có token...)

export const useUserStore = defineStore('userStore', {
    state: () => ({
        users: [],
        total: 0,
        loading: false,
        error: null
    }),

    actions: {
        async fetchUsers({ keyword = '', sort = '', page = 0, size = 10 }) {
            this.loading = true
            try {
                const res = await axios.get('/api/users', {
                    params: { keyword, sort, page, size }
                })
                this.users = res.data.content
                this.total = res.data.totalElements
            } catch (err) {
                this.error = err.response?.data?.message || 'Lỗi khi tải danh sách người dùng'
            } finally {
                this.loading = false
            }
        },

        async fetchUserById(id) {
            try {
                const res = await axios.get(`/api/users/${id}`)
                return res.data
            } catch (err) {
                throw new Error('Không tìm thấy người dùng')
            }
        },

        async createUser(userData) {
            try {
                const res = await axios.post('/api/users', userData)
                return res.data
            } catch (err) {
                console.error("Lỗi từ server:", err.response?.data || err.message)
                throw new Error('Lỗi khi tạo người dùng: ' + (err.response?.data?.message || err.message))
            }
        },

        async updateUser(id, userData) {
            try {
                await axios.put(`/api/users/${id}`, userData)
            } catch (err) {
                throw new Error('Lỗi khi cập nhật người dùng')
            }
        },

        async deleteUser(id) {
            try {
                await axios.delete(`/api/users/${id}`)
                this.users = this.users.filter(u => u.id !== id) // Cập nhật lại list trong store
            } catch (err) {
                throw new Error('Lỗi khi xoá người dùng')
            }
        }
    }
})
