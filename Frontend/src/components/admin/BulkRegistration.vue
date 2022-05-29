<!-- 批量添加用户的表单 -->
<template>
  <div style="text-align: left">
    <el-button class="add-button" type="success" @click="dialogFormVisible = true">创建用户</el-button>
    <el-dialog
      title="创建用户"
      :visible.sync="dialogFormVisible"
      @close="clear"
      width="25%">
      <el-form ref="form" :model="loginForm" :rules="rules" label-position="left"
               label-width="0px">
        <el-form-item prop="username">
          <el-input type="text" v-model="loginForm.username"
                    auto-complete="off" placeholder="账号"></el-input>
        </el-form-item>
<!--        <el-form-item prop="password">-->
<!--          <el-input type="password" v-model="loginForm.password"-->
<!--                    auto-complete="off" placeholder="密码"></el-input>-->
<!--        </el-form-item>-->
        <el-form-item>
          <el-input type="text" v-model="loginForm.name"
                    auto-complete="off" placeholder="姓名"></el-input>
        </el-form-item>
<!--        <el-form-item>-->
<!--          <el-input type="text" v-model="loginForm.phone"-->
<!--                    auto-complete="off" placeholder="电话号码"></el-input>-->
<!--        </el-form-item>-->
        <el-form-item>
          <el-input type="text" v-model="loginForm.email"
                    auto-complete="off" placeholder="邮箱"></el-input>
        </el-form-item>
        <el-form-item style="margin-left: 10px">
          <el-radio-group v-model="loginForm.role">
            <el-radio :label=5>学生</el-radio>
            <el-radio :label=3>教师</el-radio>
            <el-radio :label=4>助教</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item style="width: 100%">
          <el-button type="primary" style="width: 40%;margin-left: 30%" v-on:click="register">添加</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'BulkRegistration',
  data () {
    const validateEmail = (rule, value,callback) => {
      const reg =/^([a-zA-Z0-9]+[-_\.]?)+@[a-zA-Z0-9]+\.[a-z]+$/;
      if(value===''||value===undefined||value===null){
        callback();
      }else{
        if (!reg.test(value)){
          callback(new Error('请输入正确的邮箱'));
        } else {
          callback();
        }
      }
    }
    return {
      dialogFormVisible: false,
      rules: {
        username: [
          { required: true, message: '账号不能为空', trigger: 'blur' },
          { min: 3, max: 10, message: '长度在 3 到 10 个字符', trigger: 'blur' }
        ],
        email: [
          { required: true, message: '请输入注册邮箱', trigger: 'blur' },
          { validator: validateEmail, trigger: 'blur' }
        ],
      },
      loginForm: {
        username: '',
        password: '',
        name: '',
        email: '',
        role:5
      },

    }
  },
  methods: {
    clear () {
      this.loginForm = {
        username: '',
        password: '',
        name: '',
        email: '',
        role:5
      }
    },
    register () {
      let _this = this
      this.$refs.form.validate(async valid =>{
        if(!valid) return
        // console.log(this.loginForm)
        this.$axios
          .post('/admin/user/register', {
            username: this.loginForm.username,
            password: this.loginForm.username,
            name: this.loginForm.name,
            email: this.loginForm.email,
            role: {
              id: this.loginForm.role
            }
          })
          .then(resp => {
            if (resp.data.code === 200) {
              this.$alert('注册成功', '提示', {
                confirmButtonText: '确定'
              })
              this.clear()
              this.$emit('onSubmit')
              this.dialogFormVisible = false
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

<style scoped>
  .add-button {
    margin: 18px 0 0 10px;
  }
</style>
