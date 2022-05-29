<template>
  <div class="sign_container">
    <div class="sign_box">
      <!-- 头像区域 -->
      <div class="avatar_box">
        <img src="../assets/img/TJlogo.jpg" alt="同舟共济">
      </div>
      <!-- 登录表单区域 -->
      <el-form ref="signFormRef" :model="signForm" :rules="signFormRules" label-width="80px" class="sign_form" :label-position="labelPosition">
        <!-- 用户名 -->
        <el-form-item prop="username" label="账号" v-if="status===0">
          <el-input v-model="signForm.username" ></el-input>
        </el-form-item>
        <!-- 密码 -->
        <el-form-item prop="password" label="密码" v-if="status===2">
          <el-input v-model="signForm.password" type="password"></el-input>
        </el-form-item>
        <!-- 确认密码 -->
        <el-form-item prop="repassword" label="确认密码" v-if="status===2">
          <el-input v-model="signForm.repassword" type="password"></el-input>
        </el-form-item>
        <!-- 验证码 -->
        <el-form-item prop="verification" label="验证码" v-if="status===1">
          <el-input v-model="signForm.verification"></el-input>
        </el-form-item>
        <!-- 按钮区域 -->
        <el-form-item class="btns">
          <el-button type="info" @click="$router.replace('/login')">返回登录界面</el-button>
          <el-button type="primary" @click="sendEmail" v-if="status===0">发送验证码</el-button>
          <el-button type="primary" @click="verify" v-if="status===1">确认</el-button>
          <el-button type="primary" @click="reset" v-if="status===2">确认密码</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
export default {
  data () {
    const validatePass = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入密码'))
      }
      // else {
      //   if (this.signForm.repassword !== '') {
      //     this.$refs.signForm.validateField('repassword')
      //   }
        callback()

    }
    const validatePass2 = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入密码'))
      } else if (value !== this.signForm.password) {
        callback(new Error('两次输入密码不一致!'))
      } else {
        callback()
      }
    }
    // const validateEmail = (rule, value,callback) => {
    //   const reg =/^([a-zA-Z0-9]+[-_\.]?)+@[a-zA-Z0-9]+\.[a-z]+$/;
    //   if(value===''||value===undefined||value===null){
    //     callback();
    //   }else{
    //     if (!reg.test(value)){
    //       callback(new Error('请输入正确的邮箱'));
    //     } else {
    //       callback();
    //     }
    //   }
    // }
    return {
      status: 0,
      labelPosition: 'right',
      // 这是登录表单的数据绑定对象
      signForm: {
        userid: '',
        username: '',
        password: '',
        repassword: '',
        email: '',
        verification: ''
      },
      // 这是表单的验证规则对象
      signFormRules: {
        // 验证用户名是否合法
        // userid: [
        //   { required: true, message: '请输入登录账号', trigger: 'blur' },
        //   { min: 7, max: 7, message: '长度为 7 个字符', trigger: 'blur' }
        // ],
        username: [
          { required: true, message: '请输入您的学号/工号', trigger: 'blur' },
          { min: 3, max: 10, message: '长度在 3 到 10 个字符', trigger: 'blur' }
        ],
        // 验证密码是否合法
        password: [
          { required: true, message: '请输入登录密码', trigger: 'blur' },
          { min: 5, max: 15, message: '长度在 5 到 15 个字符', trigger: 'blur' },
          { validator: validatePass, trigger: 'blur' }
        ],
        repassword: [
          { required: true, message: '请确认登录密码', trigger: 'blur' },
          { min: 5, max: 15, message: '长度在 5 到 15 个字符', trigger: 'blur' },
          { validator: validatePass2, trigger: 'blur' }
        ],
        // email: [
        //   { required: true, message: '请输入注册邮箱', trigger: 'blur' },
        //   { validator: validateEmail, trigger: 'blur' }
        //   // { min: 6, max: 15, message: '长度在 6 到 15 个字符', trigger: 'blur' }
        // ],
        verification: [
          { required: true, message: '请输入验证码', trigger: 'blur' },
          { min: 8, max: 8, message: '长度为 8 个字符', trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    // 点击注册按钮
    reset () {
      let _this = this
      this.$refs.signFormRef.validate(async valid => {
        if (!valid) return
        if(this.signForm.password!==this.signForm.repassword){
          await this.$alert("输入密码不一致！")
          return
        }
        this.$axios
          .post('/password', {
            username: this.signForm.username,
            password: this.signForm.password,
            // name: this.signForm.name,
            // email: this.signForm.email
          })
          .then(resp => {
            console.log(resp)
            if (resp.data.code === 200) {
              this.$alert('密码设置成功', '提示', {
                confirmButtonText: '确定'
              })
              _this.$router.replace('/login')
            } else {
              this.$alert(resp.data.msg, '提示', {
                confirmButtonText: '确定'
              })
            }
          })
          .catch(failResponse => {
          })
        // this.$router.push('/login')
      })
    },
    sendEmail(){
      let _this = this
      this.$refs.signFormRef.validate(async valid => {
        if (!valid) return
        this.$axios
          .post('/sendEmail?username=' + this.signForm.username)
          .then(resp => {
            if (resp.data.code === 200) {
              this.$alert('邮件已发送，请您及时查收', '提示', {
                confirmButtonText: '确定'
              })
              // 用户输入验证码
              _this.status = 1
              // _this.$router.push('/login')
            } else {
              this.$alert(resp.data.msg, '提示', {
                confirmButtonText: '确定'
              })
            }
          })
      })
    },
    verify(){
      let _this = this
      this.$refs.signFormRef.validate(async valid => {
        if (!valid) return
        this.$axios
          .post('/verify?emailToken=' + this.signForm.verification)
          .then(resp => {
            console.log('verify:' + resp)
            if (resp.data.code === 200) {
              // 若验证码正确，用户设置密码
              _this.status = 2
            } else {
              this.$alert(resp.data.msg, '提示', {
                confirmButtonText: '确定'
              })
            }
          })
      })
    }
  }
}
</script>

<style lang="less" scoped>
.sign_container {
  //background-color: #2b4b6b;
  //height: 100%;
  background: url("../assets/img/JiadingCampus.png") no-repeat center;
  height: 100%;
  width: 100%;
  background-size: cover;
  position: fixed;
}

.sign_box {
  width: 450px;
  height: 300px;
  background-color: #dddddd;
  opacity: 0.85;
  border-radius: 25px;
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

.sign_form {
  position: absolute;
  top: 90px;
  width: 100%;
  padding: 0 20px;
  box-sizing: border-box;
}

.btns {
  //margin-bottom: 50px;
  display: flex;
  justify-content: flex-end;
}
</style>
