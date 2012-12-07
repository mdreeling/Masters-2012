module SessionsHelper
  def sign_in(user)
    cookies.permanent[:remember_token] = user.remember_token
    self.current_user = user   # NEW LINE
  end

  # NEW SETTER
  def current_user=(user)
    @current_user = user
  end

  # NEW GETTER
  def current_user
    if @current_user.nil?
      @current_user =
           User.find_by_remember_token(cookies[:remember_token])
    end
    @current_user
  end
  
  def signed_in_user
    unless signed_in?
      redirect_to signin_url, notice: "Please sign in."
    end
  end 
  
  def signed_in?
    !current_user.nil?
  end
  
end