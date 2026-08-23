package test.feature.profile

import com.bunbeauty.domain.feature.profile.model.UserRole
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class UserRoleTest {
    @Test
    fun `fromServer maps known roles`() {
        assertEquals(UserRole.MANAGER, UserRole.fromServer("manager"))
        assertEquals(UserRole.ADMIN, UserRole.fromServer("admin"))
        assertEquals(UserRole.COURIER, UserRole.fromServer("courier"))
        assertEquals(UserRole.COURIER, UserRole.fromServer("COURIER"))
    }

    @Test
    fun `fromServer returns null for unknown role`() {
        assertNull(UserRole.fromServer("unknown"))
        assertNull(UserRole.fromServer("client"))
    }

    @Test
    fun `toServer maps domain roles`() {
        assertEquals("manager", UserRole.toServer(UserRole.MANAGER))
        assertEquals("admin", UserRole.toServer(UserRole.ADMIN))
        assertEquals("courier", UserRole.toServer(UserRole.COURIER))
    }
}
