<template>
  <div>
    <el-dialog
      title="修改用户信息"
      :visible.sync="dialogFormVisible"
      width="25%">
      <el-form v-model="selectedUser" style="text-align: left" ref="dataForm">
        <el-form-item label="账号" label-width="60px" prop="username">
          <label> {{selectedUser.username}} </label>
        </el-form-item>
        <el-form-item label="姓名" label-width="60px" prop="name">
          <el-input v-model="selectedUser.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="邮箱" label-width="60px" prop="email">
          <el-input v-model="selectedUser.email" autocomplete="off"></el-input>
        </el-form-item>
<!--        <el-form-item label="角色分配" label-width="120px" prop="roles">-->
<!--          <el-checkbox-group v-model="selectedRolesIds">-->
<!--            <el-checkbox v-for="(role,i) in roles" :key="i" :label="role.id">{{role.nameZh}}</el-checkbox>-->
<!--          </el-checkbox-group>-->
<!--        </el-form-item>-->
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="onSubmit(selectedUser)">确 定</el-button>
      </div>
    </el-dialog>
    <bulk-registration @onSubmit="listUsers()" />
    <el-card style="margin: 18px 2%;width: 95%">
      <el-table
        :data="users"
        stripe
        style="width: 100%"
        :min-height="tableHeight"
        @selection-change="selsChange"
        :default-sort = "{prop: 'role.name', order: 'descending'}">
        <el-table-column
          type="selection"
          width="55">
        </el-table-column>
        <el-table-column
          type="index"
          label="序号"
          sortable
          width="100">
        </el-table-column>
        <el-table-column
          prop="username"
          label="账号"
          sortable
          fit>
        </el-table-column>
        <el-table-column
          prop="name"
          label="姓名"
          fit>
        </el-table-column>
<!--        <el-table-column-->
<!--          prop="phone"-->
<!--          label="手机号"-->
<!--          fit>-->
<!--        </el-table-column>-->
        <el-table-column
          prop="email"
          label="邮箱"
          width="200">
        </el-table-column>
        <el-table-column
          prop="role.name"
          label="身份"
          sortable
          width="130">
        </el-table-column>
        <el-table-column
          label="状态"
          width="100">
          <template slot-scope="scope">
            <el-switch
              v-model="scope.row.enabled"
              active-color="#13ce66"
              inactive-color="#ff4949"
              @change="(value) => commitStatusChange(value, scope.row)">
            </el-switch>
          </template>
        </el-table-column>
        <el-table-column
          label="操作"
          width="120">
          <template slot-scope="scope">
            <el-button
              @click="editUser(scope.row)"
              type="text"
              size="small">
              编辑
            </el-button>
            <el-button
              @click="deleteUser(scope.row)"
              type="text"
              size="small">
              移除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <div style="height: 20px"></div>
      <el-row>
        <el-col :span="6">
          <el-autocomplete
            v-model="state"
            :fetch-suggestions="querySearch"
            placeholder="请输入课程编号"
            :trigger-on-focus="false"
            @select="handleSelect"
            size="small">
<!--            <el-select v-model="select" slot="prepend" placeholder="请选择">-->
<!--              <el-option :value=0 >编号</el-option>-->
<!--              <el-option :value=1 >名称</el-option>-->
<!--            </el-select>-->
          </el-autocomplete>
        </el-col>
      </el-row>
      <div style="margin: 20px 0 20px 0;float: left">
        <el-button type="primary" @click="importBatch(sels)">批量加入课程</el-button>
        <el-button type="warning">批量删除</el-button>
      </div>
    </el-card>
  </div>
</template>

<script>
import BulkRegistration from '@/components/admin/BulkRegistration'
export default {
  name: 'UserManage',
  components: { BulkRegistration },
  data () {
    return {
      // roles: [{ id: 1, name: 'sysAdmin', nameZh: '系统管理员', enabled: true, perms: [{ id: 1, name: 'users_management', desc_: '用户管理', url: '/api/admin/user' }, { id: 2, name: 'roles_management', desc_: '角色管理', url: '/api/admin/role' }, { id: 3, name: 'content_management', desc_: '内容管理', url: '/api/admin/content' }], menus: [{ id: 1, path: '/admin', name: 'AdminIndex', nameZh: '首页', iconCls: 'el-icon-s-home', component: 'AdminIndex', parentId: 0, children: [{ id: 2, path: '/admin/dashboard', name: 'DashboardAdmin', nameZh: '运行情况', iconCls: null, component: 'dashboard/admin/index', parentId: 1, children: [] }] }, { id: 3, path: '/admin', name: 'User', nameZh: '用户管理', iconCls: 'el-icon-user', component: 'AdminIndex', parentId: 0, children: [{ id: 6, path: '/admin/user/profile', name: 'Profile', nameZh: '用户信息', iconCls: null, component: 'user/UserProfile', parentId: 3, children: [] }, { id: 7, path: '/admin/user/role', name: 'Role', nameZh: '角色配置', iconCls: null, component: 'user/Role', parentId: 3, children: [] }] }, { id: 4, path: '/admin', name: 'Content', nameZh: '内容管理', iconCls: 'el-icon-tickets', component: 'AdminIndex', parentId: 0, children: [{ id: 8, path: '/admin/content/book', name: 'BookManagement', nameZh: '图书管理', iconCls: null, component: 'content/BookManagement', parentId: 4, children: [] }, { id: 9, path: '/admin/content/banner', name: 'BannerManagement', nameZh: '广告管理', iconCls: null, component: 'content/BannerManagement', parentId: 4, children: [] }, { id: 10, path: '/admin/content/article', name: 'ArticleManagement', nameZh: '文章管理', iconCls: null, component: 'content/ArticleManagement', parentId: 4, children: [] }] }, { id: 5, path: '/admin', name: 'System', nameZh: '系统配置', iconCls: 'el-icon-s-tools', component: 'AdminIndex', parentId: 0, children: [] }] }, { id: 2, name: 'contentManager', nameZh: '内容管理员', enabled: true, perms: [{ id: 3, name: 'content_management', desc_: '内容管理', url: '/api/admin/content' }], menus: [{ id: 1, path: '/admin', name: 'AdminIndex', nameZh: '首页', iconCls: 'el-icon-s-home', component: 'AdminIndex', parentId: 0, children: [{ id: 2, path: '/admin/dashboard', name: 'DashboardAdmin', nameZh: '运行情况', iconCls: null, component: 'dashboard/admin/index', parentId: 1, children: [] }] }, { id: 4, path: '/admin', name: 'Content', nameZh: '内容管理', iconCls: 'el-icon-tickets', component: 'AdminIndex', parentId: 0, children: [{ id: 8, path: '/admin/content/book', name: 'BookManagement', nameZh: '图书管理', iconCls: null, component: 'content/BookManagement', parentId: 4, children: [] }, { id: 9, path: '/admin/content/banner', name: 'BannerManagement', nameZh: '广告管理', iconCls: null, component: 'content/BannerManagement', parentId: 4, children: [] }, { id: 10, path: '/admin/content/article', name: 'ArticleManagement', nameZh: '文章管理', iconCls: null, component: 'content/ArticleManagement', parentId: 4, children: [] }] }] }, { id: 3, name: 'visitor', nameZh: '访客', enabled: true, perms: [], menus: [{ id: 1, path: '/admin', name: 'AdminIndex', nameZh: '首页', iconCls: 'el-icon-s-home', component: 'AdminIndex', parentId: 0, children: [{ id: 2, path: '/admin/dashboard', name: 'DashboardAdmin', nameZh: '运行情况', iconCls: null, component: 'dashboard/admin/index', parentId: 1, children: [] }] }] }, { id: 9, name: 'test', nameZh: '测试角色', enabled: true, perms: [], menus: [{ id: 1, path: '/admin', name: 'AdminIndex', nameZh: '首页', iconCls: 'el-icon-s-home', component: 'AdminIndex', parentId: 0, children: [{ id: 2, path: '/admin/dashboard', name: 'DashboardAdmin', nameZh: '运行情况', iconCls: null, component: 'dashboard/admin/index', parentId: 1, children: [] }] }] }],
      users: [],
      // roles: [],
      dialogFormVisible: false,
      selectedUser: [],
      selectedRolesIds: [],
      sels: [],//选中显示的值
      state: '',
      result: [],
      select: 0,
    }
  },
  mounted () {
    this.listUsers()
    // this.listRoles()
  },
  computed: {
    tableHeight () {
      return window.innerHeight - 320
    }
  },
  methods: {
    listUsers () {
      let _this = this
      this.$axios.get('/admin/user').then(resp => {
        if (resp && resp.data.code === 200) {
          _this.users = resp.data.object
          console.log(_this.users)
        }
      })
    },
    listRoles () {
      var _this = this
      this.$axios.get('/admin/role').then(resp => {
        if (resp && resp.data.code === 200) {
          _this.roles = resp.data.result
        }
      })
    },
    commitStatusChange (value, user) {
      if (user.username !== 'admin') {
        this.$axios.post('/admin/user/status', {
          enabled: value,
          username: user.username
        }).then(resp => {
          if (resp && resp.data.code === 200) {
            if (value) {
              this.$alert('用户 [' + user.username + user.name + '] 已激活')
            } else {
              this.$alert('用户 [' + user.username + user.name + '] 已冻结')
            }
          }
        })
      } else {
        user.enabled = true
        this.$alert('不能冻结管理员账户！')
      }
    },
    onSubmit (user) {
      const _this = this
      this.$axios.post('/admin/user', {
        username: user.username,
        name: user.name,
        email: user.email,
        // roles: roles
      }).then(resp => {
        if (resp && resp.data.code === 200) {
          this.$alert('用户信息修改成功')
          this.dialogFormVisible = false
          // 修改角色后重新请求用户信息，实现视图更新
          this.listUsers()
        } else {
          this.$alert(resp.data.msg)
        }
      })
    },
    editUser (user) {
      this.dialogFormVisible = true
      this.selectedUser = user
      // const roleIds = []
      // for (let i = 0; i < user.roles.length; i++) {
      //   roleIds.push(user.roles[i].id)
      // }
      // this.selectedRolesIds = roleIds
    },
    resetPassword (username) {
      this.$axios.put('/admin/user/password', {
        username: username
      }).then(resp => {
        if (resp && resp.data.code === 200) {
          this.$alert('密码已重置为 123')
        }
      })
    },
    deleteUser(user){
      const _this = this
      this.$confirm('确定要删除该用户吗？此操作不可逆','提示',{
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(()=>{
        this.$axios.post('/admin/user/delete?username=', user.username).then(resp => {
          if (resp && resp.data.code === 200) {
            this.$alert('用户 [' + user.username + user.name + '] 已删除！')
            // 修改角色后重新请求用户信息，实现视图更新
            this.listUsers()
          } else {
            this.$alert(resp.data.msg)
          }
        })
      })
    },
    importBatch(rows){
      let _this = this
      let usernameList = [];
      rows.forEach(element =>{
        usernameList.push(element.username)
      })
      console.log(usernameList)
      this.$confirm('执行导入操作？','提示').then(() =>{
        this.$axios.post('/courses/'+_this.result[0]+'/import', usernameList).then(resp => {
          if (resp && resp.data.code === 200) {
            this.$alert("导入成功！")
          }
        })
      }).catch(()=>{});
    },
    selsChange(sels) {
      //被选中的行组成数组
      this.sels = sels
    },
    querySearch(queryString, cb) {
      let _this = this
      if (queryString !== "") {
        // 用户输入内容后做模糊查询
        setTimeout(() => {
          let suggestion = []
          this.$axios.get('/courses/findlike?keyword='+queryString+'&type='+this.select).then(res => {
            if (res && res.data.code === 200) {
              let callarr = []
              let callBack = res.data.object
              callarr.push(callBack)
              console.log('查询信息：', callarr)
              // 只有value键能被读取为匹配内容
              // console.log(this.select)
              // if(this.select===0){
                suggestion = callarr.map((item)=>{
                  return {
                    value: String(item['id']),
                    // key: String(item['id'])
                  }
                })
              // }
              // else {
              //   suggestion = callarr.map((item)=>{
              //     // console.log(item)
              //     return {
              //       value: item['courseName'],
              //     }
              //   })
              // }
              console.log("响应转换结果：", suggestion)
              if (suggestion.length === 0) {
                cb([{ value: "暂无数据"}]);
              }
              // 查到数据
              else {
                cb(suggestion);
              }
            }
          })
        }, 1000);
      }
    },
    handleSelect(item) {
      this.result.push(item.value)
    },
  }
}
</script>

<style scoped>

</style>
