import layoutHeaderAside from '@/layout/header-aside'

const meta = { requiresAuth: true }

export default {
  path: '/develop',
  name: 'develop',
  meta,
  component: layoutHeaderAside,
  children: (pre => [
    { path: 'swagger-ui', name: `${pre}swagger-ui`, component: () => import('@/pages/develop/swagger-ui'), meta: { meta, title: '接口文档' } },
    { path: 'eureka', name: `${pre}eureka`, component: () => import('@/pages/develop/eureka'), meta: { meta, title: '服务注册中心' } },
    { path: 'apollo', name: `${pre}apollo`, component: () => import('@/pages/develop/apollo'), meta: { meta, title: '配置中心' } }
  ])('develop-')
}
