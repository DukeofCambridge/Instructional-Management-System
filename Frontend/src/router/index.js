import Vue from 'vue'
import VueRouter from 'vue-router'
import Login from '../components/Login.vue'
import Sign from '../components/Reset.vue'
import Home from '../components/Home.vue'
import Error from '@/components/Error'
import AdminIndex from '@/components/admin/AdminIndex'
import UserManage from '@/components/admin/UserManage'
import CourseIndex from '@/components/courses/CourseIndex'
import CourseHome from '@/components/courses/CourseHome'
import CourseProjects from '@/components/courses/CourseProjects'
import TeacenterIndex from '@/components/teacenter/TeacenterIndex'
import Teacenterall from '@/components/teacenter/Teacenterall'
import Teacentermy from '@/components/teacenter/Teacentermy'
import CourseFiles from '@/components/courses/CourseFiles'
import ProjectIndex from '@/components/courses/ProjectIndex'
import Account from '@/components/user/Account'
import CourseMembers from "@/components/courses/CourseMembers";
import CourseGrades from "@/components/courses/CourseGrades";
import ValidateSuccess from "@/components/user/ValidateSuccess";
import AdminLogin from "@/components/admin/AdminLogin";
import CourseManage from "@/components/admin/CourseManage";
import ReportEditor from "@/components/courses/ReportEditor";
import Contest from "@/components/courses/Contest";
import Show from "@/components/courses/statistics/Show";
import Comment from "@/components/courses/Comment";
import Browse from "../components/courses/Browse";
Vue.use(VueRouter)

const routes = [
  { path: '/', redirect: '/login' },
  {
    path: '/login',
    name: 'Login',
    component: Login
  },
  {
    path: '/reset',
    name: 'Sign',
    component: Sign
  },
  {
    path: '/home',
    name: 'Home',
    component: Home
  },
  {
    path: '/account',
    name: 'Account',
    component: Account
  },
  {
    path: '/admin',
    name: 'AdminLogin',
    component: AdminLogin
  },
  {
    path: '/adminindex',
    name: 'AdminIndex',
    component: AdminIndex,
    redirect: '/adminindex/user',
    children: [
      {
        path: 'user',
        name: 'AdminUser',
        component: UserManage
      },
      {
        path: 'courses',
        name: 'AdminCourse',
        component: CourseManage
      },
      {
        path: 'course/:id',
        name: 'CourseMembers',
        component: CourseMembers
      }
    ]
  },
  {
    path: '/courses/:id',
    name: 'Courses',
    props: true,
    component: CourseIndex,
    redirect: '/courses/:id/home',
    children: [
      {
        path: 'home',
        name: 'CourseHome',
        component: CourseHome
      },
      {
        path: 'report/:projectId/:username',
        name: 'Re',
        component: Browse
      },
      {
        path: 'projects',
        name: 'CourseProjects',
        component: CourseProjects
      },
      {
        path: 'files',
        name: 'CourseFiles',
        component: CourseFiles
      },
      {
        path: 'project/:projectId',
        name: 'ProjectIndex',
        component: ProjectIndex
      },
      {
        path: 'contest/:projectId',
        name: 'ContestIndex',
        component: Contest
      },
      {
        path: 'report/:projectId',
        name:'Report',
        component: ReportEditor
      },
      {
        path: 'members',
        name: 'CourseMembers',
        component: CourseMembers
      },
      {
        path: 'grades',
        name: 'CourseGrades',
        component: CourseGrades
      },
      {
        path: 'statistics',
        name: 'Statistics',
        component: Show
      },
      {
        path: 'discuss',
        name: 'Discuss',
        component: Comment
      }
    ]
  },
  {
    path: '/teacenter',
    name: 'Teacenter',
    redirect: '/teacentermy',
    component: TeacenterIndex,
    children: [
      { path: '/teacenterall', component: Teacenterall },
      { path: '/teacentermy', component: Teacentermy }
    ]
  },
  {
    path: '/valsuccess',
    name: 'ValSuccess',
    component: ValidateSuccess
  },
  {
    path: '*',
    name: 'Error',
    component: Error
  }
]

const router = new VueRouter({
  mode: 'history',
  routes
})

export default router
