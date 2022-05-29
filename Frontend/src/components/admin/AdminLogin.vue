<template>
  <div class="login_container">
    <div class="login_box">
      <!-- 头像区域 -->
      <div class="avatar_box">
        <img src="../../assets/img/TJlogo.jpg" alt="同舟共济">
      </div>

      <!-- 登录表单区域 -->
      <el-form :label-position="labelPosition" ref="loginFormRef" :model="loginForm" :rules="loginFormRules" label-width="60px" class="login_form" >
        <h3 style="margin: 10px 138px">管理员后台登录</h3>
        <!-- 账号就是用户名，就是学号 -->
        <el-form-item  prop="username" label="账号">
          <el-input v-model="loginForm.username" prefix-icon="el-icon-user"></el-input>
        </el-form-item>
        <!-- 密码 -->
        <el-form-item  prop="password" label="密码">
          <el-input v-model="loginForm.password" type="password" prefix-icon="el-icon-unlock"></el-input>
        </el-form-item>
        <!-- 按钮区域 -->
        <el-form-item class="btns">
          <el-button type="primary" @click="login">登录</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
export default {
  name: 'AdminLogin',
  data () {
    return {
      // 这是登录表单的数据绑定对象
      labelPosition: 'right',
      loginForm: {
        username: '',
        password: ''
      },
      // 这是表单的验证规则对象
      loginFormRules: {
        // 验证用户名是否合法
        username: [
          { required: true, message: '请输入登录账号', trigger: 'blur' },
          // { min: 7, max: 7, message: '长度为 7 个字符', trigger: 'blur' }
        ],
        // 验证密码是否合法
        password: [
          { required: true, message: '请输入登录密码', trigger: 'blur' },
          { min: 6, max: 15, message: '长度在 6 到 15 个字符', trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    login () {
      this.$refs.loginFormRef.validate(async valid => {
        this.$axios
          .post('/login', {
            username: this.loginForm.username,
            password: this.loginForm.password
          })
          .then(resp => {
            if (resp.data.code === 200) {
              // console.log(resp.data.object)
              if(resp.data.object.role.id!==1){
                this.$alert("请使用管理员账号登录！")
                return
              }
              this.$store.commit('login', resp.data.object)
              // console.log(localStorage.user)
              this.$router.replace('/adminindex')
            } else {
              this.$alert(resp.data.msg, '提示', {
                confirmButtonText: '确定'
              })
            }
          })
          .catch(failResponse => {})
      })
    }
  }
}
</script>

<style lang="less" scoped>
.login_container {
  //background-color: #2b4b6b;
  //height: 100%;
  background: url("../../assets/img/JiadingCampus.png") no-repeat center;
  height: 100%;
  width: 100%;
  background-size: cover;
  position: fixed;
}

.login_box {
  width: 450px;
  height: 300px;
  background-color: #dddddd;
  opacity: 0.85;
  border-radius: 3px;
  position: absolute;
  left: 50%;
  top: 50%;
  transform: translate(-50%, -50%);

  .avatar_box {
    height: 130px;
    width: 130px;
    border: 1px solid #eee;
    border-radius: 50%;
    padding: 10px;
    box-shadow: 0 0 10px #ddd;
    position: absolute;
    left: 50%;
    transform: translate(-50%, -50%);
    background-color: #fff;
    img {
      width: 100%;
      height: 100%;
      border-radius: 50%;
      background-color: #eee;
    }
  }
}

.login_form {
  position: absolute;
  bottom: 0;
  width: 100%;
  padding: 0 20px;
  box-sizing: border-box;
}

.btns {
  display: flex;
  justify-content: flex-end;
}
</style>
