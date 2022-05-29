<template>
  <el-container id="courses-body">
    <el-header style="margin-left: -20px">
      <navbar />
    </el-header>
    <!--<el-row style="height: 100%;">-->
    <el-card style="margin: 18px 30%;width: 40%;min-height: 280px;padding-bottom: 20px;opacity: 0.9">
        <el-form style="text-align: left;margin-top: 40px" ref="dataForm">
          <el-form-item label="学工号" label-width="120px" prop="username">
            <label>{{username}}</label>
          </el-form-item>
          <el-form-item label="真实姓名" label-width="120px" prop="name">
            <label>{{ name }}</label>
          </el-form-item>
          <el-form-item label="手机号" label-width="120px" prop="phone">
            <el-input v-model="phone" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item label="邮箱" label-width="120px" prop="email">
            <el-input v-model="email" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item label="密码" label-width="120px" prop="password">
            <el-button type="warning" @click="resetPassword">重置密码</el-button>
          </el-form-item>
          <el-form-item label="您的身份" label-width="120px" prop="roles">
            <el-button type="primary"> {{role}}</el-button>
            <!--          <label>{{role}}</label>-->
          </el-form-item>
        </el-form>
        <div style="margin: 20px 10px 20px 10px">
        <!--        <el-button @click="dialogFormVisible = false">取 消</el-button>-->
          <el-button type="primary" @click="onSubmit" style="float: right">修 改</el-button>
        </div>
    </el-card>
  </el-container>
</template>

<script>
import Navbar from '@/components/common/Navbar'
export default {
  name: 'AccountSetting',
  components: { Navbar },
  data () {
    return {
      name: '',
      username: '',
      email: '',
      phone: '',
      role: ''

    }
  },
  created() {
    this.name = this.$store.state.user.name
    this.username = this.$store.state.user.username
    this.email = this.$store.state.user.email
    this.phone = this.$store.state.user.phone
    this.role = this.$store.state.user.role.name

  },
  methods: {
    resetPassword () {
      this.$router.push('/reset')
    },
    onSubmit() {
      this.$axios.post('/updateInfo', {
        username: this.username,
        phone: this.phone,
        email: this.email
      }).then(resp => {
        if (resp && resp.data.code === 200) {
          this.$alert('用户信息修改成功')
        } else {
          this.$alert(resp.data.msg)
        }
      })
    }
  },

}
</script>

<style scoped>
#courses-body {
  background: url("../../assets/img/background.jpg") no-repeat;
  height: 100%;
  width: 100%;
}
</style>
