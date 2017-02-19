function [J, grad] = linearRegCostFunction(X, y, theta, lambda)
%LINEARREGCOSTFUNCTION Compute cost and gradient for regularized linear 
%regression with multiple variables
%   [J, grad] = LINEARREGCOSTFUNCTION(X, y, theta, lambda) computes the 
%   cost of using theta as the parameter for linear regression to fit the 
%   data points in X and y. Returns the cost in J and the gradient in grad

% Initialize some useful values
m = length(y); % number of training examples

% You need to return the following variables correctly 
J = 0;
grad = zeros(size(theta));

% ====================== YOUR CODE HERE ======================
% Instructions: Compute the cost and gradient of regularized linear 
%               regression for a particular choice of theta.
%
%               You should set J to the cost and grad to the gradient.
%

A = X * theta - y;
n = size(theta, 1);
s = 0;
for j = 2:n
  s = s + theta(j, 1) ^ 2;
endfor

J = (A' * A + lambda * s) / (2 * m);


theta1 = theta;
theta1(1,1) = 0;
grad = (X' * A + lambda * theta1) / m;
% =========================================================================

grad = grad(:);

end
